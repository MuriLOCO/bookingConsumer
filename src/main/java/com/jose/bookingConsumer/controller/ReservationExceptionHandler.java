package com.jose.bookingConsumer.controller;


import com.jose.bookingConsumer.exception.ReservationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ReservationExceptionHandler {

   @ExceptionHandler(ReservationException.class)
   public ResponseEntity<Object> handleReservationException(ReservationException ex){
       Map<String, Object> body = new LinkedHashMap<>();
       body.put("timestamp", Instant.now());
       body.put("message", ex.getMessage());
       return new ResponseEntity<>(body, HttpStatus.NOT_ACCEPTABLE);
   }
}
