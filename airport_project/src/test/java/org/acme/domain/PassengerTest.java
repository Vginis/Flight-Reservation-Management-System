package org.acme.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PassengerTest {
    Passenger passenger;
    Reservation reservation;
    Flight flight;

    @BeforeEach
    public void setup() {
        Airline a2 = new Airline("RayanAir", "FR", "rayan", "01234567890");
        Airport ai1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
        Airport ai2 = new Airport("Fumicino","Milan","Italy","FCO");
        passenger = new Passenger("email@gmail.com", "100", "AK111111","pppp", "01234567890");
        flight = new Flight("FR8438", a2, ai1, "09:00", ai2, "12:00",  200,"Boeing-365", 100L);
        reservation= new Reservation();
        Ticket t1 = new Ticket(reservation, flight, "1A", "Bob", "Wonder", "CP152D45");
        reservation.setPassenger(passenger);
        reservation.addOutgoingFlight(flight);
        reservation.addTicket(t1);
        passenger.addReservation(reservation);
    }

    @Test
    public void DenyReservationForAnOtherPassenger(){
        Passenger passenger2 = new Passenger("emp@gmail.com", "100254435", "AK155644","oooo", "01234567890");
        Assertions.assertThrows(RuntimeException.class, () -> {
            passenger2.addReservation(reservation);
        });
    }

    @Test
    public void DenyDuplicateReservation(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            passenger.addReservation(reservation);
        });
    }

    @Test
    public void DenyRemovingNonExistingReservation(){
        passenger.removeReservation(reservation);
        Assertions.assertThrows(RuntimeException.class, () -> {
            passenger.removeReservation(reservation);
        });
    }

    @Test
    public void DenyInvalidEmail(){
        Assertions.assertThrows(RuntimeException.class, () -> {
            passenger.setEmail("dfsdfs");
        });
    }
}
