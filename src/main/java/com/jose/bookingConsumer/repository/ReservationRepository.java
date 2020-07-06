package com.jose.bookingConsumer.repository;

import com.jose.bookingConsumer.domain.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends MongoRepository<Reservation, UUID> {

    Reservation findByDaysReservedContainsAndCancelled(List<Instant> daysReserved, Boolean cancelled);
}
