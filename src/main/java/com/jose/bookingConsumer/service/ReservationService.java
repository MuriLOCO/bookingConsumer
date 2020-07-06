package com.jose.bookingConsumer.service;

import com.jose.bookingConsumer.domain.Person;
import com.jose.bookingConsumer.domain.Reservation;
import com.jose.bookingConsumer.dto.ReservationDTO;
import com.jose.bookingConsumer.exception.ReservationException;
import com.jose.bookingConsumer.repository.ReservationRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final MailSenderService mailSenderService;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, MailSenderService mailSenderService){
        this.reservationRepository = reservationRepository;
        this.mailSenderService = mailSenderService;
    }
    /**
     * Makes a pre-validation of the reservation
     * @param reservationDTO
     */
    @SneakyThrows
    private void preValidationOfReservation(ReservationDTO reservationDTO){

        //Checks if the reservation is of Max 3 days
        if(ChronoUnit.DAYS.between(reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving()) > 3){
            String errorMessage = "You can only reserve for Max 3 days.";
            mailSenderService.sendEmailReservationFailed(reservationDTO, errorMessage);
            throw new ReservationException(errorMessage);
        }

        //Checks if the reservation and the day of arrival are made at max 1 day of ahead or 1 month in advance

        //TODO: now we are assuming that the month is 30 days, in the future we can improve this feature
        if(ChronoUnit.DAYS.between(reservationDTO.getDayOfReservation(), reservationDTO.getDayOfArrival()) < 1 && (ChronoUnit.DAYS.between(reservationDTO.getDayOfReservation(), reservationDTO.getDayOfArrival()) > 30)) throw new ReservationException("The reservation must be made at Max one day ahead of arrival or 1 month in advance.");
    }

    /**
     * Calculates and returns the actual days of reservation
     * @param reservationDTO
     * @return
     */
    private static List<Instant> getReservedDays(ReservationDTO reservationDTO){
        long numberOfDays =  ChronoUnit.DAYS.between(reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving());
        List<Instant> daysReserved = new ArrayList<>();
        for(int i = 0; i < numberOfDays; i++){
            daysReserved.add(reservationDTO.getDayOfArrival().plusSeconds(86400*i)); //86400 is the amount of seconds per day
        }
        return daysReserved;
    }

    /**
     * Validates and reserves if the validation passes
     * @param reservationDTO
     * @return
     */
    @SneakyThrows
    public Reservation makeReservation(ReservationDTO reservationDTO){

        //Pre-validates the reservation
        preValidationOfReservation(reservationDTO);

        //Checks if there is any reservation that conflicts of any of the days chosen to stay
        Reservation existingReservation = reservationRepository.findByDaysReservedContainsAndCancelled(getReservedDays(reservationDTO), false);
        if(existingReservation != null){
            String errorMessage = "Sorry, the campsite is not available during the days reserved.";
            mailSenderService.sendEmailReservationFailed(reservationDTO, errorMessage);
            throw new ReservationException(errorMessage);
        }

        //If everything is fine, book the reservation
        Person person = new Person();
        person.setFirstMame(reservationDTO.getFirstMame());
        person.setLastName(reservationDTO.getLastName());
        person.setDataOfBirth(reservationDTO.getDateOfBirth());
        person.setEmailAddress(reservationDTO.getEmailAddress());
        person.setPhoneNumber(reservationDTO.getPhoneNumber());

        Reservation reservation = new Reservation();
        reservation.setPerson(person);
        reservation.setCancelled(reservationDTO.getCancelled());
        reservation.setDateOfReservation(reservationDTO.getDayOfReservation());
        reservation.setDayOfArrival(reservationDTO.getDayOfArrival());
        reservation.setDayOfLeaving(reservationDTO.getDayOfLeaving());
        reservation.setDaysReserved(getReservedDays(reservationDTO));

        mailSenderService.sendEmailReservationConfirmed(reservationDTO);
        return reservationRepository.save(reservation);
    }

}
