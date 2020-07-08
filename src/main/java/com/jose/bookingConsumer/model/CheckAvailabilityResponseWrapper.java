package com.jose.bookingConsumer.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Set;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class CheckAvailabilityResponseWrapper {

    private Boolean available;
    private Set<Instant> unavailableDays;

}
