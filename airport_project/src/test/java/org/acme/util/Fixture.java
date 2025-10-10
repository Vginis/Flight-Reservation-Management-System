package org.acme.util;

import org.acme.representation.ReservationRepresentation;
import org.acme.representation.TicketRepresentation;
import org.acme.representation.airline.AirlineRepresentation;
import org.acme.representation.airport.AirportRepresentation;
import org.acme.representation.flight.FlightRepresentation;

import java.util.ArrayList;

public class Fixture {

    public final static String API_ROOT  = "http://localhost:8081";

    public static AirportRepresentation getAirportRepresentation(){
        AirportRepresentation dto = new AirportRepresentation();
        dto.setAirportId(1);
        dto.setAirportName("Furina");
        dto.setCity("Fontaine");
        dto.setCountry("Teyvat");
        dto.setU3digitCode("BEY");
//        dto.depFlights = new ArrayList<>();
//        dto.depFlights.add(7);
//        dto.arrFlights = new ArrayList<>();
//        dto.arrFlights.add(9);
        return dto;
    }

    public static FlightRepresentation getFlightRepresentation(){
        FlightRepresentation dto = new FlightRepresentation ();
//        dto.id = 190;
//        dto.flightNo = "A3651";
//        dto.airlineName = "Aegean Airlines";
//        dto.departureAirport = "Fiumicino";
//        dto.departureTime = LocalDateTime.parse("2024-02-12T10:12:12");
//        dto.arrivalAirport = "Eleftherios Venizelos";
//        dto.arrivalTime = LocalDateTime.parse("2024-02-12T18:24:36");
//        dto.aircraftCapacity = 120;
//        dto.aircraftType = "BSA-4545";
//        dto.ticketPrice = (long) 80;
//        dto.availableSeats = 24;
//        dto.ticketList = new ArrayList<>();
//        dto.ticketList.add(getTicketRepresentation().ticketId);
        return dto;
    }

    public static AirlineRepresentation getAirlineRepresentation(){
        AirlineRepresentation dto = new AirlineRepresentation();
        dto.setId(12);
        dto.setAirlineName("British Airways");
        dto.setU2digitCode("BR");
        return dto;
    }

    public static TicketRepresentation getTicketRepresentation() {
        TicketRepresentation representation = new TicketRepresentation();
        representation.ticketId = 11;
        representation.reservationId = 9;
        representation.flightNo = "FR8438";
        representation.firstName = "Bob";
        representation.lastName = "Wander";
        representation.passportId = "CP152D35=";
        representation.seatNo = "1A";
        representation.luggageIncluded = false;
        representation.amount = 0;
        representation.weight = 0;
        representation.ticketPrice = 100L;
        return representation;
    }

    public static ReservationRepresentation getReservationRepresentation() {
        ReservationRepresentation representation = new ReservationRepresentation();
        representation.passengerId = 6;
        representation.outgoingFlights = new ArrayList<>();
        //representation.outgoingFlights.add(getFlightRepresentation().flightNo);
        representation.ingoingFlights = new ArrayList<>();
        representation.ingoingFlights.add("FR8438");
        representation.ticketList = new ArrayList<>();
        representation.ticketList.add(getTicketRepresentation());
        representation.totalPrice = 240L;
        return representation;
    }

    //public static FlightRepresentation getFlightRepresentation(){}
}
