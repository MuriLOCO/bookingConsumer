package com.jose.bookingConsumer.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CancellationResponseWrapper {

    public CancellationResponseWrapper(String reservationId, Boolean cancelled){
        this.reservationId = reservationId;
        this.cancelled = cancelled;
    }

    private String reservationId;
    private Boolean cancelled;
    private Instant cancellationDate = Instant.now();
}
