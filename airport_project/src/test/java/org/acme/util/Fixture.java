package org.acme.util;

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
        dto.airportId = 5;
        dto.airportName = "Furina_De_Chateau";
        dto.city = "Cardinale";
        dto.country = "Fontaine";
        dto.u3digitCode = "FON";
        return dto;
    }

    public static FlightRepresentation getFlightRepresentation(){
        FlightRepresentation dto = new FlightRepresentation ();
        dto.id = 10;
        dto.flightNo = "BS3456";
        dto.airlineName = "Grand_Cross";
        dto.departureAirport = "Camelot";
        dto.departureTime = LocalDateTime.parse("2024-02-12T10:12:12");
        dto.arrivalAirport = "Teyvat";
        dto.arrivalTime = LocalDateTime.parse("2024-02-12T18:24:36");
        dto.aircraftCapacity = 120;
        dto.aircraftType = "BSA-4545";
        dto.ticketPrice = (long) 48;
        dto.availableSeats = 24;
        dto.ticketList = new ArrayList<Integer>();
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
        representation.ticketId = 14;
        representation.reservationId = 20;
        representation.flightNo = "A3654";
        representation.firstName = "Alice";
        representation.lastName = "Waves";
        representation.passportId = "GH76J90";
        representation.seatNo = "11A";
        representation.luggageIncluded = false;
        representation.amount = 0;
        representation.weight = 0;
        representation.ticketPrice = 240L;
        return representation;
    }

    public static ReservationRepresentation getReservationRepresentation() {
        ReservationRepresentation representation = new ReservationRepresentation();
        representation.passengerId = 6;
        representation.outgoingFlights = new ArrayList<>();
        representation.ingoingFlights = new ArrayList<>();
        representation.ticketList = new ArrayList<>();
        representation.ticketList.add(getTicketRepresentation());
        representation.totalPrice = 240L;
        return representation;
    }

    //public static FlightRepresentation getFlightRepresentation(){}
}
