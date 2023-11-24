package org.acme.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AirlineTest {
    Airline airline;
    //Flight flight1;
    Airport airport1;
    Airport airport2;
    Airport airport3;

    @BeforeEach
    public void setup(){
        airline = new Airline("Aegean Airlines", "A3", "aegean", "JeandDig1@");
        airport1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATH");
        airport2 = new Airport("Fumicino","Milan","Italy","FCO");
        airport3 = new Airport("Thessalonikis","Thessaloniki","Greece","SKG");
    }

    @Test
    public void denyExistingFlights(){
        Flight flight1 = new Flight("A3651", airline, airport2, "19:00", airport1, "21:00", 178, "Airbus-A320", 80L);
        airline.addFlight(flight1);
        Assertions.assertThrows(RuntimeException.class, () -> {
            airline.addFlight(flight1);
        });
    }

    @Test
    public void denyFlightFromAnotherAirline(){
        Airline airline2 = new Airline("Transavia", "TV", "trans", "JeandDig1@");
        Flight flight2 = new Flight("A3651", airline2, airport2, "19:00", airport1, "21:00", 178, "Airbus-A320", 80L);
        Assertions.assertThrows(RuntimeException.class, () -> {
            airline.addFlight(flight2);
        });
    }

    @Test
    public void denyNonExistingDelete (){
        Airline airline2 = new Airline("Transavia", "TV", "trans", "JeandDig1@");
        Flight flight2 = new Flight("A3651", airline2, airport2, "19:00", airport1, "21:00", 178, "Airbus-A320", 80L);
        Assertions.assertThrows(RuntimeException.class, () -> {
            airline.removeFlight(flight2);
        });
    }

    @Test
    public void computeMostPopularAirport(){
        Flight flight1 = new Flight("A3651", airline, airport2, "19:00", airport1, "21:00", 178, "Airbus-A320", 80L);
        Flight flight2 = new Flight("A3651", airline, airport1, "19:00", airport3, "21:00", 178, "Airbus-A320", 80L);
        airline.addFlight(flight1);
        airline.addFlight(flight2);
        Assertions.assertEquals("Eleftherios Venizelos",airline.mostPopularAirport());
    }

    }

