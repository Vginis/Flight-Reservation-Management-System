package org.acme.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        flightO = new Flight("A3651", airline, airport1, "19:00", airport2, "21:00", 178, "Airbus-A320", 80L);
        flightI = new Flight("A3650", airline, airport2, "22:00", airport1, "00:00", 178, "Airbus-A320", 50L);
        reservation = new Reservation();
        ticket = new Ticket(reservation, flightO, "11A", "Art", "Bunny", "34K89JL7");
    }

    @Test
    public void AddSameOutgoingFlight2Times() {
        reservation.addOutgoingFlight(flightO);
        Assertions.assertThrows(RuntimeException.class, () -> reservation.addOutgoingFlight(flightO));
    }

    @Test
    public void RemoveNoExistOutgoingFlight() {
        reservation.addOutgoingFlight(flightO);
        Assertions.assertThrows(RuntimeException.class, () -> reservation.removeOutgoingFlight(flightI));
    }

    @Test
    public void AddSameIngoingFlight2Times() {
        reservation.addIngoingFlight(flightI);
        Assertions.assertThrows(RuntimeException.class, () -> reservation.addIngoingFlight(flightI));
    }

    @Test
    public void RemoveNoExistIngoingFlight() {
        reservation.addIngoingFlight(flightI);
        Assertions.assertThrows(RuntimeException.class, () -> reservation.removeIngoingFlight(flightO));
    }

    @Test
    public void TotalPriceCalculation() {
        reservation.addTicket(ticket);
        assertEquals(80L, reservation.getTotalPrice());
    }

}
