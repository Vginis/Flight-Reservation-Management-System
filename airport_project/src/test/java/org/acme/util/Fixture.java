package org.acme.util;

import org.acme.representation.AirlineRepresentation;
import org.acme.representation.AirportRepresentation;

public class Fixture {

    public static String API_ROOT  = "http://localhost:8081";

    public static AirportRepresentation getAirportRepresentation(){
        AirportRepresentation dto = new AirportRepresentation ();
        dto.name = "My Airport";
        dto.city = "Agios Dimitrios";
        dto.country = "Greece";
        dto.u3digitCode = "BRH";

        return dto;
    }

    public static AirlineRepresentation getAirlineRepresentation(){
        AirlineRepresentation dto = new AirlineRepresentation();
        dto.airlineId = 1000;
        dto.name = "British Airways";
        dto.u2digitCode = "BR";
        return dto;
    }
}
