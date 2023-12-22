package org.acme.util;

import org.acme.representation.AirlineRepresentation;
import org.acme.representation.AirportRepresentation;

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
}
