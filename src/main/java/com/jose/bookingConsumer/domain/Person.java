package com.jose.bookingConsumer.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Document
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@RequiredArgsConstructor
public class Person {

    @EqualsAndHashCode.Exclude
    @Id
    //Mongo has issues to Query using UUID so the ID needs to be converted to a String before saving
    private String id = UUID.randomUUID().toString();

    @NonNull
    private String firstMame;

    @NonNull
    private String lastName;

    @NonNull
    private String phoneNumber;

    @NonNull
    private String emailAddress;

    @NonNull
    private Instant dataOfBirth;
}
