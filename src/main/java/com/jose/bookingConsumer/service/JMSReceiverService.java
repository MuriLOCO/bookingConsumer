package com.jose.bookingConsumer.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jose.bookingConsumer.dto.ReservationDTO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class JMSReceiverService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JMSReceiverService.class);
    private static final String QUEUE_NAME_RESERVATION = "RESERVATION";
    private static final String QUEUE_NAME_MODIFICATION = "MODIFY";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private ReservationService reservationService;

    @SneakyThrows
    @JmsListener(destination = QUEUE_NAME_RESERVATION)
    public void receiveReservation(String message) {
        CountDownLatch latch = new CountDownLatch(1);
        LOGGER.info("Received message='{}'", message);
        latch.countDown();

        objectMapper.registerModule(new JavaTimeModule());
        ReservationDTO reservationDTO = objectMapper.readValue(message, ReservationDTO.class);
        reservationService.makeReservation(reservationDTO);
    }

    @SneakyThrows
    @JmsListener(destination = QUEUE_NAME_MODIFICATION)
    public void receiveModification(String message) {
        CountDownLatch latch = new CountDownLatch(1);
        LOGGER.info("Received message='{}'", message);
        latch.countDown();

        objectMapper.registerModule(new JavaTimeModule());
        ReservationDTO reservationDTO = objectMapper.readValue(message, ReservationDTO.class);
        reservationService.modifyReservation(reservationDTO);
    }
}
