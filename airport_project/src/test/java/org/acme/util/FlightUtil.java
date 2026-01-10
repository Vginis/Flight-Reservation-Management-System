package org.acme.util;

import org.acme.domain.Flight;
import org.acme.domain.FlightSeatLayout;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.representation.flight.FlightRepresentation;

import java.time.LocalDateTime;

public class FlightUtil {
    public static FlightRepresentation createFlightRepresentation(){
        FlightRepresentation flightRepresentation = new FlightRepresentation();
        flightRepresentation.setId(1);
        flightRepresentation.setFlightNumber("AA-121");
        flightRepresentation.setFlightUUID("uuid");
        flightRepresentation.setArrivalAirport(AirportUtil.createAirportRepresentation("ATH"));
        flightRepresentation.setDepartureAirport(AirportUtil.createAirportRepresentation("SKG"));
        flightRepresentation.setArrivalTime(LocalDateTime.now());
        flightRepresentation.setDepartureTime(LocalDateTime.now());
        return flightRepresentation;
    }

    public static Flight createFlight(){
        return new Flight(createFlightCreateRepresentation(), AirlineUtil.createAirline(),
                AirportUtil.createAirport("ATH"), AirportUtil.createAirport("SKG"),
                new FlightSeatLayout(AircraftUtil.createAircraft()));
    }

    public static FlightCreateRepresentation createFlightCreateRepresentation(){
        FlightCreateRepresentation flightCreateRepresentation = new FlightCreateRepresentation();
        flightCreateRepresentation.setAircraftId(1);
        flightCreateRepresentation.setFlightNumber("AA-121");
        flightCreateRepresentation.setArrivalAirport("ATH");
        flightCreateRepresentation.setDepartureAirport("SKG");
        flightCreateRepresentation.setArrivalTime(LocalDateTime.now());
        flightCreateRepresentation.setDepartureTime(LocalDateTime.now());
        return flightCreateRepresentation;
    }

    public static FlightDateUpdateRepresentation createFlightDateUpdateRepresentation(){
        FlightDateUpdateRepresentation flightDateUpdateRepresentation = new FlightDateUpdateRepresentation();
        flightDateUpdateRepresentation.setArrivalTime(LocalDateTime.now());
        flightDateUpdateRepresentation.setDepartureTime(LocalDateTime.now());
        return flightDateUpdateRepresentation;
    }
}
