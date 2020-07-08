package com.jose.bookingConsumer.controller;

import com.jose.bookingConsumer.model.CancellationResponseWrapper;
import com.jose.bookingConsumer.model.CheckAvailabilityResponseWrapper;
import com.jose.bookingConsumer.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("reserve")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<CheckAvailabilityResponseWrapper> checkAvailability(
             @RequestParam(required = false) Instant beginDate,
             @RequestParam(required = false) Instant endDate){
        return ResponseEntity.ok(reservationService.checkAvailability(beginDate, endDate));
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public ResponseEntity<CancellationResponseWrapper> cancelReservation(
            @RequestParam String reservationId){
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

}
