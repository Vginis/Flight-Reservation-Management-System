package org.acme.util;

import org.acme.domain.Aircraft;
import org.acme.representation.aircraft.AircraftCreateRepresentation;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.acme.representation.aircraft.AircraftUpdateRepresentation;

public class AircraftUtil {
    public static AircraftCreateRepresentation createAircraftCreateRepresentation(){
        AircraftCreateRepresentation aircraftCreateRepresentation = new AircraftCreateRepresentation();
        aircraftCreateRepresentation.setAircraftName("Aircraft 1");
        aircraftCreateRepresentation.setAircraftCapacity(100);
        aircraftCreateRepresentation.setAirlineId(1);
        return aircraftCreateRepresentation;
    }

    public static Aircraft createAircraft(){
        return new Aircraft(createAircraftCreateRepresentation(), AirlineUtil.createAirline());
    }

    public static AircraftRepresentation createAircraftRepresentation(){
        AircraftRepresentation aircraftRepresentation = new AircraftRepresentation();
        aircraftRepresentation.setId(1);
        aircraftRepresentation.setAircraftName("Aircraft 1");
        aircraftRepresentation.setAircraftCapacity(200);
        aircraftRepresentation.setAirline2DigitCode("AA");
        return aircraftRepresentation;
    }

    public static AircraftUpdateRepresentation createAircraftUpdateRepresentation(){
        AircraftUpdateRepresentation aircraftUpdateRepresentation = new AircraftUpdateRepresentation();
        aircraftUpdateRepresentation.setAircraftName("Aircraft 2");
        aircraftUpdateRepresentation.setAircraftCapacity(101);
        return aircraftUpdateRepresentation;
    }
}
