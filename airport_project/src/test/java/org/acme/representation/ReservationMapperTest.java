package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Reservation;
import org.acme.domain.Ticket;
import org.acme.mapper.ReservationMapper;
import org.acme.persistence.ReservationRepository;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ReservationMapperTest {

    @Inject
    ReservationMapper reservationMapper;

    @Inject
    ReservationRepository reservationRepository;

//    @Test
//    @Transactional
//    public void testToRepresentation() {
//        Reservation reservation = reservationRepository.findById(9);
//        ReservationRepresentation reservationRepresentation = reservationMapper.toRepresentation(reservation);
//
//        assertEquals(reservation.getReservationId(), reservationRepresentation.reservationId);
//        assertEquals(reservation.getPassenger().getId(), reservationRepresentation.passengerId);
//        assertEquals(1, reservationRepresentation.outgoingFlights.size());
//        for (String r : reservationRepresentation.outgoingFlights) {
//            String d = Objects.requireNonNull(reservation.getOutgoingFlights().stream().filter(a -> a.getFlightNo().contains(r))
//                    .findFirst().orElse(null)).getFlightNo();
//            assertEquals(d, r);
//        }
//        assertEquals(0, reservationRepresentation.ingoingFlights.size());
//        assertEquals(1, reservationRepresentation.ticketList.size());
//        List<Ticket> tickets = new ArrayList<>(reservation.getTicketsList());
//        for (TicketRepresentation r : reservationRepresentation.ticketList) {
//            Ticket t = tickets.stream().filter(a -> a.getPassportId().contains(r.passportId)).findFirst().orElse(null);
//            assert t != null;
//            assertEquals(t.getTicketId(), r.ticketId);
//            //assertEquals(t.getReservation().getReservationId(), r.reservation);
//            assertEquals(t.getFlight().getFlightNo(), r.flightNo);
//            assertEquals(t.getFirstName(), r.firstName);
//            assertEquals(t.getLastName(), r.lastName);
//            assertEquals(t.getPassportId(), r.passportId);
//            assertEquals(t.getSeatNo(), r.seatNo);
//            assertEquals(t.getAmount(), r.amount);
//            assertEquals(t.getWeight(), r.weight);
//            assertEquals(t.getTicketPrice(), r.ticketPrice);
//        }
//    }
//
//    @Test
//    @Transactional
//    public void testToModel() {
//        ReservationRepresentation reservationRepresentation = Fixture.getReservationRepresentation();
//        Reservation entity = reservationMapper.toModel(reservationRepresentation);
//
//        assertEquals(entity.getReservationId(), reservationRepresentation.reservationId);
//        assertEquals(entity.getPassenger().getId(), reservationRepresentation.passengerId);
//        assertEquals(1, reservationRepresentation.outgoingFlights.size());
//        assertEquals(1, reservationRepresentation.ingoingFlights.size());
//        assertEquals(1, reservationRepresentation.ticketList.size());
//        assertEquals(entity.getTotalPrice(), reservationRepresentation.totalPrice);
//    }

}
