package org.acme.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;

public class AdministratorTest {

    Airline airline, airline1;
    Airport airport1, airport2, airport3;
    Administrator ad1;
    Passenger p1, p2;

    @BeforeEach
    public void setup() {
        p1 = new Passenger("limbo@gmail.com", "1234567453", "AK102222", "limbo", "Filuc@#1!");
        p2 = new Passenger("nico@gmail.com", "4444567453", "AK103434", "nico", "Jiluc@#1!");
        airline = new Airline("Aegean Airlines", "A3", "aegean", "Diluc2fj#");
        airline1 = new Airline("Ryan Airlines", "R4", "ryanair", "Biluc2fj#");
        ad1 = new Administrator();
        airport2 = new Airport("Fumicino", "Milan", "Italy", "FCO");
        airport3 = new Airport("Thessalonikis", "Thessaloniki", "Greece", "SKG");
    }


    @Test
    public void AirportAdded() {
        airport1 = new Airport("Agisilaos Petrou", "Thessaloniki", "Greece", "THE");
        ad1.addAirport(airport1);
        Assertions.assertThrows(RuntimeException.class, () -> ad1.addAirport(airport1));
    }

    @Test
    public void RemoveNoExistAirport() {
        ad1.addAirport(airport2);
        Assertions.assertThrows(RuntimeException.class, () -> ad1.removeAirport(airport3));
    }

    @Test
    public void RemoveNoExistAirline() {
        airline = new Airline("Aegean Airlines", "A4", "aegean", "Ziluc2fj#");
        ad1.addAirline(airline);
        Assertions.assertThrows(RuntimeException.class, () -> ad1.removeAirline(airline1));
    }

    @Test
    public void RemoveNoExistPassenger() {
        ad1.addPassenger(p1);
        Assertions.assertThrows(RuntimeException.class, () -> ad1.removePassenger(p2));
    }
}