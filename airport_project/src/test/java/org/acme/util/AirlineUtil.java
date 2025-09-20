package org.acme.util;

import org.acme.domain.Airline;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.airline.AirlineRepresentation;

public class AirlineUtil {
    public static AirlineCreateRepresentation createAirlineCreateRepresentation(){
        return new AirlineCreateRepresentation("airline 1", "AA");
    }

    public static Airline createAirline(){
        return new Airline(createAirlineCreateRepresentation());
    }

    public static AirlineRepresentation createAirlineRepresentation(){
        AirlineRepresentation airlineRepresentation = new AirlineRepresentation();
        airlineRepresentation.setId(1);
        airlineRepresentation.setAirlineName("airline 1");
        airlineRepresentation.setU2digitCode("AA");
        return airlineRepresentation;
    }
}
