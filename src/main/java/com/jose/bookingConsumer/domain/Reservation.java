package com.jose.bookingConsumer.domain;


import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Document
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@RequiredArgsConstructor
public class Reservation {

    @EqualsAndHashCode.Exclude
    @Id
    private UUID id = UUID.randomUUID();

    @CreatedDate
    @Indexed(expireAfter = "1m")
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
