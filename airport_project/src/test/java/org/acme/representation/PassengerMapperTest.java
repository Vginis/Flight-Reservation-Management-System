package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Passenger;
import org.acme.mapper.PassengerMapper;
import org.acme.persistence.PassengerRepository;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PassengerMapperTest {

    @Inject
    PassengerMapper passengerMapper;

    @Inject
    PassengerRepository passengerRepository;

//    @Test
//    @Transactional
//    public void testToRepresentation() {
//        Passenger passenger = passengerRepository.findById(6);
//        PassengerRepresentation passengerRepresentation = passengerMapper.toRepresentation(passenger);
//
//        assertEquals(passenger.getId(), passengerRepresentation.id);
//        assertEquals(passenger.getUsername(), passengerRepresentation.username);
//        assertEquals(passenger.getEmail(), passengerRepresentation.email);
//        for (Integer r : passengerRepresentation.reservationsId) {
//            Integer b = Objects.requireNonNull(passenger.getReservations().stream().filter(a -> a.getReservationId().equals(r)).findFirst().orElse(null)).getReservationId();
//            assert b != null;
//            assertEquals(b, r);
//        }
//    }
//
//    @Test
//    @Transactional
//    public void testToModel() {
//        PassengerRepresentation passengerRepresentation = Fixture.getPassengerRepresentation();
//        Passenger entity = passengerMapper.toModel(passengerRepresentation);
//
//        assertEquals(entity.getId(), passengerRepresentation.id);
//        assertEquals(entity.getUsername(), passengerRepresentation.username);
//        assertEquals(entity.getEmail(), passengerRepresentation.email);
//        assertEquals(entity.getPassport_id(), passengerRepresentation.passport_id);
//        assertEquals(1, passengerRepresentation.reservationsId.size());
//    }

}
