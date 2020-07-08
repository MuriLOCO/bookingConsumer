package com.jose.bookingConsumer.service;

import com.jose.bookingConsumer.dto.ReservationDTO;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailSenderService {

    private static final String SUBJECT_RESERVATION_CONFIRMED = "Your reservation is Confirmed!";
    private static final String SUBJECT_RESERVATION_FAILED = "Reservation Failed.";
    private static final String TEXT_RESERVATION_CONFIRMED = "Hi %s,\nYour reservation to the campsite is confirmed from the day %s to %s, your reservation code is %s.\nSee you soon!";
    private static final String TEXT_RESERVATION_FAILED = "Hi %s,\nUnfortunately your reservation to the campsite is NOT confirmed from the day %s to %s, the reason is: %s";

    private static final String SUBJECT_MODIFICATION_CONFIRMED = "Your modification is Confirmed!";
    private static final String SUBJECT_MODIFICATION_FAILED = "Modification Failed.";
    private static final String TEXT_MODIFICATION_CONFIRMED = "Hi %s,\nYour modification of the reservation to the campsite is confirmed from the day %s to %s, your reservation code is %s.\nSee you soon!";
    private static final String TEXT_MODIFICATION_FAILED = "Hi %s,\nUnfortunately your modification of reservation to the campsite is NOT confirmed from the day %s to %s, the reason is: %s";

    private final JavaMailSender mailSender;


    @Autowired
    public MailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Sends a confirmation that your attempt to reserve the campsite was received.
     * @param reservationDTO
     */
    @Async
    @SneakyThrows
        public void sendEmailReservationConfirmed(ReservationDTO reservationDTO) {

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(reservationDTO.getEmailAddress());

            msg.setSubject(SUBJECT_RESERVATION_CONFIRMED);
            msg.setText(String.format(TEXT_RESERVATION_CONFIRMED, reservationDTO.getFirstMame(), reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving(), reservationDTO.getReservationId()));
            mailSender.send(msg);
        }

    /**
     * Sends a confirmation that your attempt to reserve the campsite was received.
     * @param reservationDTO
     */
    @Async
    @SneakyThrows
    public void sendEmailReservationFailed(ReservationDTO reservationDTO, String reason) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(reservationDTO.getEmailAddress());

        msg.setSubject(SUBJECT_RESERVATION_FAILED);
        msg.setText(String.format(TEXT_RESERVATION_FAILED, reservationDTO.getFirstMame(), reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving(), reason));
        mailSender.send(msg);
    }


    /**
     * Sends a confirmation that your attempt to reserve the campsite was received.
     * @param reservationDTO
     */
    @Async
    @SneakyThrows
    public void sendModificationEmailReservationConfirmed(ReservationDTO reservationDTO) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(reservationDTO.getEmailAddress());

        msg.setSubject(SUBJECT_MODIFICATION_CONFIRMED);
        msg.setText(String.format(TEXT_MODIFICATION_CONFIRMED, reservationDTO.getFirstMame(), reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving(), reservationDTO.getReservationId()));
        mailSender.send(msg);
    }

    /**
     * Sends a confirmation that your attempt to reserve the campsite was received.
     * @param reservationDTO
     */
    @Async
    @SneakyThrows
    public void sendEmailModificationReservationFailed(ReservationDTO reservationDTO, String reason) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(reservationDTO.getEmailAddress());

        msg.setSubject(SUBJECT_MODIFICATION_FAILED);
        msg.setText(String.format(TEXT_MODIFICATION_FAILED, reservationDTO.getFirstMame(), reservationDTO.getDayOfArrival(), reservationDTO.getDayOfLeaving(), reason));
        mailSender.send(msg);
    }
    }
