package org.acme.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReservationTest {

    Passenger passenger;
    Airline airline;
    Airport airport1;
    Airport airport2;
    Flight flightO;
    Flight flightI;
    Ticket ticket;
    Reservation reservation;

    @BeforeEach
    public void setup() {
        passenger = new Passenger("artb@gmail.com", "00306947165246", "34K89JL7", "artb", "JeandDig1@");
        airline = new Airline("Aegean Airlines", "A3", "aegean", "JeandDig1@");
        airport1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
        airport2 = new Airport("Fiumicino","Rome","Italy","FCO");
        flightO = new Flight("A3651", airline, airport1, "202307192100", airport2, "202307192100", 178, "Airbus-A320", 80L);
        flightI = new Flight("A3650", airline, airport2, "202307192100", airport1, "202307192100", 178, "Airbus-A320", 50L);
        reservation = new Reservation();
        ticket = new Ticket(reservation, flightO, "11A", "Art", "Bunny", "34K89JL7");
    }

    @Test
    public void AddSameOutgoingFlight2Times() {
        reservation.addOutgoingFlight(flightO);
        assertThrows(RuntimeException.class, () -> reservation.addOutgoingFlight(flightO));
    }

    @Test
    public void RemoveExistingOutgoingFlight() {
        reservation.addOutgoingFlight(flightO);
        assertEquals(1, reservation.getOutgoingFlights().size());
        reservation.removeOutgoingFlight(flightO);
        assertEquals(0, reservation.getOutgoingFlights().size());
    }

    @Test
    public void RemoveNoExistOutgoingFlight() {
        reservation.addOutgoingFlight(flightO);
        assertThrows(RuntimeException.class, () -> reservation.removeOutgoingFlight(flightI));
    }

    @Test
    public void AddSameIngoingFlight2Times() {
        reservation.addIngoingFlight(flightI);
        assertThrows(RuntimeException.class, () -> reservation.addIngoingFlight(flightI));
    }

    @Test
    public void RemoveExistingIngoingFlight() {
        reservation.addIngoingFlight(flightO);
        assertEquals(1, reservation.getIngoingFlights().size());
        reservation.removeIngoingFlight(flightO);
        assertEquals(0, reservation.getIngoingFlights().size());
    }

    @Test
    public void RemoveNoExistIngoingFlight() {
        reservation.addIngoingFlight(flightI);
        assertThrows(RuntimeException.class, () -> reservation.removeIngoingFlight(flightO));
    }

    @Test
    public void AddSameTicket2Times() {
        reservation.addTicket(ticket);
        assertThrows(RuntimeException.class, () -> reservation.addTicket(ticket));
    }

    @Test
    public void RemoveExistingTicket() {
        reservation.addTicket(ticket);
        assertEquals(1, reservation.getTicketsList().size());
        reservation.removeTicket(ticket);
        assertEquals(0, reservation.getTicketsList().size());
    }

    @Test
    public void RemoveNoExistingTicket() {
        Ticket ticket1 = new Ticket(reservation, flightO, "11A", "Art", "Bunny", "34K89JL7");
        reservation.addTicket(ticket1);
        assertThrows(RuntimeException.class, () -> reservation.removeTicket(ticket));
    }

    @Test
    public void TotalPriceCalculation() {
        reservation.addTicket(ticket);
        assertEquals(80L, reservation.getTotalPrice());
    }

}
