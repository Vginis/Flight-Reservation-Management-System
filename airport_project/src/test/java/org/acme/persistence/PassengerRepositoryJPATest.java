package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.domain.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class PassengerRepositoryJPATest extends JPATest {

    @Inject
    PassengerRepository passengerRepository;

    @Test
    void listPassengers() {
        List<?> result = em.createQuery("select p from Passenger p").getResultList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void test_findPassengerByPassport() {
        Optional<Passenger> passenger = passengerRepository.findPassengerByPassport("P12345678");
        Assertions.assertTrue(passenger.isPresent());
    }

    @Test
    void test_findPassengerByUsername() {
        Optional<Passenger> passenger = passengerRepository.findPassengerByUsername("passenger1");
        Assertions.assertTrue(passenger.isPresent());
    }
}
