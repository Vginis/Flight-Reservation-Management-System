package org.acme.util;

import org.acme.domain.Airport;
import org.acme.representation.airport.AirportCreateRepresentation;
import org.acme.representation.airport.AirportRepresentation;
import org.acme.representation.airport.AirportUpdateRepresentation;

public class AirportUtil {
    public static AirportRepresentation createAirportRepresentation(){
        AirportRepresentation airportRepresentation = new AirportRepresentation();
        airportRepresentation.setAirportName("airportName");
        airportRepresentation.setAirportId(1);
        airportRepresentation.setCity("Athens");
        airportRepresentation.setCountry("Greece");
        airportRepresentation.setU3digitCode("ATH");
        return airportRepresentation;
    }

    public static Airport createAirport(){
        return new Airport("airportName", "Athens","Greece", "ATH");
    }

    public static AirportCreateRepresentation createAirportCreateRepresentation(){
        AirportCreateRepresentation airportCreateRepresentation = new AirportUpdateRepresentation();
        airportCreateRepresentation.setAirportName("airport 1");
        airportCreateRepresentation.setCity("Athens");
        airportCreateRepresentation.setCountry("Greece");
        airportCreateRepresentation.setU3digitCode("ATH");
        return airportCreateRepresentation;
    }

    public static AirportUpdateRepresentation createAirportUpdateRepresentation(){
        AirportUpdateRepresentation airportUpdateRepresentation = new AirportUpdateRepresentation();
        airportUpdateRepresentation.setId(1);
        airportUpdateRepresentation.setAirportName("airport 1");
        airportUpdateRepresentation.setCity("Athens");
        airportUpdateRepresentation.setCountry("Greece");
        airportUpdateRepresentation.setU3digitCode("ATH");
        return airportUpdateRepresentation;
    }
}
