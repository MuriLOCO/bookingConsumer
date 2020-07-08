package com.jose.bookingConsumer.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class ReservationDTO implements Serializable {


    private UUID reservationId;
    private Instant dayOfArrival;
    private Instant dayOfLeaving;
    private Instant dayOfReservation;
    private Boolean cancelled;
    private String firstMame;
    private String lastName;
    private String phoneNumber;
    private String emailAddress;
    private Instant dateOfBirth;
}
