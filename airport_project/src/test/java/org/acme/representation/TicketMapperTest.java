package org.acme.representation;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Ticket;
import org.acme.mapper.TicketMapper;
import org.acme.persistence.TicketRepository;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class TicketMapperTest {

    @Inject
    TicketMapper ticketMapper;

    @Inject
    TicketRepository ticketRepository;

    @Test
    @Transactional
    public void testToRepresentation() {
        Ticket ticket = ticketRepository.findById(11);
        TicketRepresentation ticketRepresentation = ticketMapper.toRepresentation(ticket);

        assertEquals(ticket.getTicketId(), ticketRepresentation.ticketId);
        assertEquals(ticket.getReservation().getReservationId(), ticketRepresentation.reservationId);
        assertEquals(ticket.getFlight().getFlightNo(), ticketRepresentation.flightNo);
        assertEquals(ticket.getFirstName(), ticketRepresentation.firstName);
        assertEquals(ticket.getLastName(), ticketRepresentation.lastName);
        assertEquals(ticket.getPassportId(), ticketRepresentation.passportId);
        assertEquals(ticket.getSeatNo(), ticketRepresentation.seatNo);
        assertEquals(ticket.isLuggageIncluded(), ticketRepresentation.luggageIncluded);
        assertEquals(ticket.getAmount(), ticketRepresentation.amount);
        assertEquals(ticket.getWeight(), ticketRepresentation.weight);
        assertEquals(ticket.getTicketPrice(), ticketRepresentation.ticketPrice);
    }

    @Test
    @Transactional
    public void testToModel() {
        TicketRepresentation ticketRepresentation =Fixture.getTicketRepresentation();
        Ticket entity = ticketMapper.toModel(ticketRepresentation);

        assertEquals(entity.getTicketId(), ticketRepresentation.ticketId);
        assertEquals(entity.getReservation().getReservationId(), ticketRepresentation.reservationId);
        assertEquals(entity.getFlight().getFlightNo(), ticketRepresentation.flightNo);
        assertEquals(entity.getFirstName(), ticketRepresentation.firstName);
        assertEquals(entity.getLastName(), ticketRepresentation.lastName);
        assertEquals(entity.getPassportId(), ticketRepresentation.passportId);
        assertEquals(entity.getSeatNo(), ticketRepresentation.seatNo);
        assertEquals(entity.isLuggageIncluded(), ticketRepresentation.luggageIncluded);
        assertEquals(entity.getAmount(), ticketRepresentation.amount);
        assertEquals(entity.getWeight(), ticketRepresentation.weight);
        assertEquals(entity.getTicketPrice(), ticketRepresentation.ticketPrice);
    }

}
