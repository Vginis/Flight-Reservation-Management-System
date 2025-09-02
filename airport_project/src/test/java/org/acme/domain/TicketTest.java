package org.acme.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TicketTest {

    Ticket ticket;

    @BeforeEach
    public void setup() {
        Reservation reservation = new Reservation();
        Airline airline = new Airline("Aegean Airlines", "A3");//, "aegean", "JeandDig1@");
        Airport airport1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
        Airport airport2 = new Airport("Fiumicino","Rome","Italy","FCO");
        Flight flight = new Flight("A3651", airline, airport1, "202307192100", airport2, "202307192100", 178, "Airbus-A320", 80L);
        ticket = new Ticket(reservation, flight, "11A", "Bob", "Dumb", "G478LJH");
    }

    @Test
    public void CalculateTicketPrice() {
        assertEquals(80, ticket.getTicketPrice());
        ticket.setLuggageIncluded(true);
        assertEquals(110, ticket.getTicketPrice());

        assertEquals(0, ticket.getWeight());
        ticket.setWeight(20);
        assertEquals(20, ticket.getWeight());

        assertEquals(0, ticket.getAmount());
        ticket.setAmount(1);
        assertEquals(1, ticket.getAmount());
    }

}
