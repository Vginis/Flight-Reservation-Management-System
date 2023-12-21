package org.acme.util;

import org.acme.representation.AirportRepresentation;

public class Fixture {

    public static AirportRepresentation getAirportRepresentation(){
        AirportRepresentation dto = new AirportRepresentation ();
        dto.airportId = 3;
        dto.name = "MyAirport";
        dto.city = "Agios Dimitrios";
        dto.country = "Greece";
        dto.u3digitCode = "BRH";

        return dto;
    }
}
