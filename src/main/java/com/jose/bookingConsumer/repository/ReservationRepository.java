package com.jose.bookingConsumer.repository;

import com.jose.bookingConsumer.domain.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Set;


@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {

    Reservation findByDaysReservedContainsAndCancelled(List<Instant> daysReserved, Boolean cancelled);
    Set<Reservation> findByDaysReservedInAndCancelled(List<Instant> daysReserved, Boolean cancelled);
}
