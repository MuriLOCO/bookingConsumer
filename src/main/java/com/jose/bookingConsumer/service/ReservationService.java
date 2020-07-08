package com.jose.bookingConsumer.service;

import com.jose.bookingConsumer.domain.Person;
import com.jose.bookingConsumer.domain.Reservation;
import com.jose.bookingConsumer.dto.ReservationDTO;
import com.jose.bookingConsumer.exception.ReservationException;
import com.jose.bookingConsumer.model.CancellationResponseWrapper;
import com.jose.bookingConsumer.model.CheckAvailabilityResponseWrapper;
import com.jose.bookingConsumer.repository.ReservationRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MailSenderService mailSenderService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MailSenderService mailSenderService) {
        this.reservationRepository = reservationRepository;
        this.mailSenderService = mailSenderService;
    }

    /**
     * Makes a pre-validation of the reservation
     *
     * @param reservationDTO
     */
    @SneakyThrows
    private void preValidationOfReservation(ReservationDTO reservationDTO) {

        //Checks if the reservation is of Max 3 days
        if (ChronoUnit.DAYS.between(reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving()) > 3) {
            String errorMessage = "You can only reserve for Max 3 days.";
            mailSenderService.sendEmailReservationFailed(reservationDTO, errorMessage);
            throw new ReservationException(errorMessage);
        }

        //Checks if the reservation and the day of arrival are made at max 1 day of ahead or 1 month in advance

        //TODO: now we are assuming that the month is 30 days, in the future we can improve this feature
        if (ChronoUnit.DAYS.between(reservationDTO.getDayOfReservation(), reservationDTO.getDayOfArrival()) < 1 && (ChronoUnit.DAYS.between(reservationDTO.getDayOfReservation(), reservationDTO.getDayOfArrival()) > 30))
            throw new ReservationException("The reservation must be made at Max one day ahead of arrival or 1 month in advance.");
    }

    /**
     * Calculates and returns the actual days of reservation
     *
     * @param reservationDTO
     * @return
     */
    private static List<Instant> getReservedDays(ReservationDTO reservationDTO) {
        return getReservedDays(reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving());
    }

    /**
     * Calculates and returns the actual days of reservation
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    private static List<Instant> getReservedDays(Instant beginDate, Instant endDate) {
        long numberOfDays = ChronoUnit.DAYS.between(beginDate, endDate);
        List<Instant> daysReserved = new ArrayList<>();
        for (int i = 0; i < numberOfDays; i++) {
            daysReserved.add(beginDate.plusSeconds(86400 * i)); //86400 is the amount of seconds per day
        }
        return daysReserved;
    }

    /**
     * Creates or modifies the reservation, if the reservation object is passed it will modify it
     * @param reservationDTO
     * @param providedReservation
     * @return
     */
    @SneakyThrows
    private Reservation createOrModifyReservation(ReservationDTO reservationDTO, Reservation providedReservation){
        preValidationOfReservation(reservationDTO);

        //Checks if there is any reservation that conflicts of any of the days chosen to stay
        Reservation existingReservation = reservationRepository.findByDaysReservedContainsAndCancelled(getReservedDays(reservationDTO), false);
        if (existingReservation != null) {
            String errorMessage = "Sorry, the campsite is not available during the days reserved.";
            if(providedReservation == null) mailSenderService.sendEmailReservationFailed(reservationDTO, errorMessage);
            else mailSenderService.sendEmailModificationReservationFailed(reservationDTO, errorMessage);
            throw new ReservationException(errorMessage);
        }

        //If everything is fine, book the reservation

        Person person = providedReservation == null ? new Person() : providedReservation.getPerson();
        person.setFirstMame(reservationDTO.getFirstMame());
        person.setLastName(reservationDTO.getLastName());
        person.setDataOfBirth(reservationDTO.getDateOfBirth());
        person.setEmailAddress(reservationDTO.getEmailAddress());
        person.setPhoneNumber(reservationDTO.getPhoneNumber());

        Reservation reservation = providedReservation == null ? new Reservation() : providedReservation;
        reservation.setId(providedReservation == null ? reservationDTO.getReservationId().toString() : providedReservation.getId());
        reservation.setPerson(person);
        reservation.setCancelled(reservationDTO.getCancelled());
        reservation.setDateOfReservation(reservationDTO.getDayOfReservation());
        reservation.setDayOfArrival(reservationDTO.getDayOfArrival());
        reservation.setDayOfLeaving(reservationDTO.getDayOfLeaving());
        reservation.setDaysReserved(getReservedDays(reservationDTO));

        return reservation;
    }

    /**
     * Validates and reserves if the validation passes
     *
     * @param reservationDTO
     * @return
     */
    @SneakyThrows
    public Reservation makeReservation(ReservationDTO reservationDTO) {

        mailSenderService.sendEmailReservationConfirmed(reservationDTO);
        return reservationRepository.save(createOrModifyReservation(reservationDTO, null));
    }

    /**
     * Checks if the campsite is available for the desired dates and return the unavailable dates accordingly
     *
     * @param beginDate
     * @param endDate
     * @return
     */

    //TODO: this method could be Async but would require to get a response via e-mail which is not what we want here
    //TODO: however, using MongoDB will greatly increase the performance upon many requests at the same time
    public CheckAvailabilityResponseWrapper checkAvailability(Instant beginDate, Instant endDate) {
        //If beginDate of endDate is not set, the system falls under the default 1 month counting from today
        if (beginDate == null || endDate == null) {
            beginDate = Instant.now();
            endDate = beginDate.plusSeconds(86400 * 30); //One month
        }

        Set<Reservation> unavailableDaysOfReservation = reservationRepository.findByDaysReservedInAndCancelled(getReservedDays(beginDate, endDate), false);
        Set<Instant> unavailableDays = unavailableDaysOfReservation.stream().map(Reservation::getDaysReserved).flatMap(List::stream).collect(Collectors.toSet());
        if (!unavailableDays.isEmpty()) {
            return new CheckAvailabilityResponseWrapper(false, unavailableDays);
        }
        return new CheckAvailabilityResponseWrapper(true, null);
    }

    /**
     * Cancels the reservation
     *
     * @param reservationId
     * @return
     */
    @SneakyThrows
    public CancellationResponseWrapper cancelReservation(String reservationId) {

        Reservation reservation = getReservation(reservationId);
        reservation.setCancelled(true);

        reservationRepository.save(reservation);
        return new CancellationResponseWrapper(reservationId, true);
    }

    @SneakyThrows
    public Reservation modifyReservation(ReservationDTO reservationDTO) {
        Reservation existingReservation = getReservation(reservationDTO.getReservationId().toString());
        if(existingReservation == null){
            String errorMessage = String.format("Reservation %s does not exist.", reservationDTO.getReservationId());
            mailSenderService.sendEmailModificationReservationFailed(reservationDTO, errorMessage);
            throw new ReservationException(errorMessage);
        }


        Reservation modifiedReservation = createOrModifyReservation(reservationDTO, existingReservation);
        mailSenderService.sendModificationEmailReservationConfirmed(reservationDTO);
        return reservationRepository.save(modifiedReservation);
    }



    /**
     * Finds the reservation, validates and returns
     * @param reservationId
     * @return
     */
    @SneakyThrows
    private Reservation getReservation(String reservationId){
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);

        if(!reservation.isPresent()) throw new ReservationException(String.format("Reservation %s does not exist.", reservationId));
        return reservation.get();
    }
}

