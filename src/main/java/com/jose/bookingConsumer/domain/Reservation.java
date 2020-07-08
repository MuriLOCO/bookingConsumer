package com.jose.bookingConsumer.domain;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Document
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@RequiredArgsConstructor
public class Reservation {

    @Indexed
    @EqualsAndHashCode.Exclude
    @Id
    //Mongo has issues to Query using UUID so the ID needs to be converted to a String before saving
    private String id;

    @CreatedDate
    @EqualsAndHashCode.Exclude
    private Instant createdAt = Instant.now();

    @NonNull
    private Instant dateOfReservation;

    @NonNull
    private Instant dayOfArrival;

    @NonNull
    private Instant dayOfLeaving;

    @NonNull
    private Boolean cancelled;

    @NonNull
    private Person person;

    @NonNull
    private List<Instant> daysReserved;
}
