package org.acme.domain;

import net.bytebuddy.implementation.bind.MethodDelegationBinder;
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

    @Test
    public void computeCompletness(){
        Passenger passenger = new Passenger("artb@gmail.com", "00306947165246", "34K89JL7", "artb", "JeandDig1@");
        Passenger passenger2 = new Passenger("bob@gmail.com", "00306947165233", "34K89KL7", "bobm", "JeandDig1@");
        Flight flight1 = new Flight("A3651", airline, airport2, "19:00", airport1, "21:00", 5, "Airbus-A320", 80L);
        Flight flight2 = new Flight("A3652", airline, airport1, "19:00", airport3, "21:00", 10, "Airbus-A321", 80L);

        Reservation reservation1 = new Reservation();
        reservation1.setPassenger(passenger);

        Reservation reservation2 = new Reservation();
        reservation2.setPassenger(passenger2);

        Reservation reservation3 = new Reservation();
        reservation2.setPassenger(passenger2);


        Ticket ticket1 = new Ticket(reservation1, flight1, "11A", "Art", "Bunny", "34K89JL7");
        Ticket ticket2 = new Ticket(reservation2, flight1, "11Β", "Bob", "Marley", "34K89KL7");
        Ticket ticket3 = new Ticket(reservation3, flight2, "11Β", "Bob", "Marley", "34K89KL7");


        reservation1.addTicket(ticket1);
        reservation2.addTicket(ticket2);
        reservation3.addTicket(ticket3);

        flight1.addTicket(ticket1);
        flight1.addTicket(ticket2);
        flight2.addTicket(ticket3);
        airline.addFlight(flight1);
        airline.addFlight(flight2);
        Assertions.assertEquals(25,airline.completeness());
    }
    }

