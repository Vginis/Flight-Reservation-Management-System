package org.acme.util;

import org.acme.representation.*;

import java.util.ArrayList;

public class Fixture {

    public static String API_ROOT  = "http://localhost:8081";

    public static class Reservations {
        public static int RESERVATION_ONE_WAY_ID = 9;
    }

    public static class Airlines {
        public static int AIRLINE_ID=5;
    }

    public static AirportRepresentation getAirportRepresentation(){
        AirportRepresentation dto = new AirportRepresentation ();
        dto.airportId = 4;
        dto.name = "Furina_De_Chateau";
        dto.city = "Cardinale";
        dto.country = "Fontaine";
        dto.u3digitCode = "FON";
        return dto;
    }

    public static AirlineRepresentation getAirlineRepresentation(){
        AirlineRepresentation dto = new AirlineRepresentation();
        dto.id = 12;
        dto.name = "British Airways";
        dto.u2digitCode = "BR";
        dto.username="british";
        dto.password="JeandDig1@";
        return dto;
    }

    public static PassengerRepresentation getPassengerRepresentation(){
        PassengerRepresentation representation = new PassengerRepresentation();
        representation.id = 44;
        representation.email = "email@gmail.com";
        representation.password = "VGinis12@djsj";
        representation.username = "passenger123";
        representation.passportId = "AK810399";
        representation.phoneNumber = "8388383838";
        return representation;
    }

    public static TicketRepresentation getTicketRepresentation() {
        TicketRepresentation representation = new TicketRepresentation();
        //representation.reservation = getReservationRepresentation();
        representation.firstName = "Alice";
        representation.lastName = "Waves";
        representation.passportId = "GH76J90";
        representation.seatNo = "11A";
        representation.luggageIncluded = false;
        representation.ticketPrice = 240L;
        return representation;
    }

    public static ReservationRepresentation getReservationRepresentation() {
        ReservationRepresentation representation = new ReservationRepresentation();
        //representation.passenger = ???
        //representation.outgoingFlights = new ArrayList<>();
        //representation.ingoingFlights = new ArrayList<>();
        representation.ticketsList = new ArrayList<>();
        representation.ticketsList.add(getTicketRepresentation());
        representation.totalPrice = 240L;
        return representation;
    }

}
