package org.acme.util;

import org.acme.persistence.JPATest;
import org.acme.representation.*;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class Fixture {

    public static String API_ROOT  = "http://localhost:8081";

    public static class Reservations {
        public static int RESERVATION_ONE_WAY_ID = 9;
    }

    public static class Airlines {
        public static int AIRLINE_ID = 5;
    }

    public static AirportRepresentation getAirportRepresentation(){
        AirportRepresentation dto = new AirportRepresentation ();
        dto.airportId = 199;
        dto.airportName = "Furina";
        dto.city = "Cardinale";
        dto.country = "Fontaine";
        dto.u3digitCode = "FON";
        return dto;
    }

    public static FlightRepresentation getFlightRepresentation(){
        FlightRepresentation dto = new FlightRepresentation ();
        dto.id = 190;
        dto.flightNo = "A3651";
        dto.airlineName = getAirlineRepresentation().airlineName;
        dto.departureAirport = getAirportRepresentation().airportName;
        dto.departureTime = LocalDateTime.parse("2024-02-12T10:12:12");
        dto.arrivalAirport = getAirportRepresentation().airportName;
        dto.arrivalTime = LocalDateTime.parse("2024-02-12T18:24:36");
        dto.aircraftCapacity = 120;
        dto.aircraftType = "BSA-4545";
        dto.ticketPrice = (long) 80;
        dto.availableSeats = 24;
        dto.ticketList = new ArrayList<Integer>();
        dto.ticketList.add(getTicketRepresentation().ticketId);
        return dto;
    }

    public static AirlineRepresentation getAirlineRepresentation(){
        AirlineRepresentation dto = new AirlineRepresentation();
        dto.id = 12;
        dto.airlineName = "British Airways";
        dto.u2digitCode = "BR";
        dto.username="british";
        dto.password="JeandDig1@";
        dto.flights = new ArrayList<>();
        return dto;
    }

    public static PassengerRepresentation getPassengerRepresentation(){
        PassengerRepresentation representation = new PassengerRepresentation();
        representation.id = 2;
        representation.email = "email@gmail.com";
        representation.password = "VGinis12@djsj";
        representation.username = "passenger123";
        representation.passport_id = "AK810399";
        representation.phoneNum = "8388383838";
        representation.reservationsId = new ArrayList<>();
        return representation;
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
        representation.outgoingFlights.add(getFlightRepresentation().flightNo);
        representation.ingoingFlights = new ArrayList<>();
        representation.ticketList = new ArrayList<>();
        representation.ticketList.add(getTicketRepresentation());
        representation.totalPrice = 240L;
        return representation;
    }

    //public static FlightRepresentation getFlightRepresentation(){}
}
