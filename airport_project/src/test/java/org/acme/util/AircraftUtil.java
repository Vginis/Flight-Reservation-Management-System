package org.acme.util;

import org.acme.domain.Aircraft;
import org.acme.domain.Airline;
import org.acme.representation.aircraft.AircraftCreateUpdateRepresentation;
import org.acme.representation.aircraft.AircraftRepresentation;

public class AircraftUtil {
    public static AircraftCreateUpdateRepresentation createAircraftCreateRepresentation(){
        AircraftCreateUpdateRepresentation aircraftCreateRepresentation = new AircraftCreateUpdateRepresentation();
        aircraftCreateRepresentation.setAircraftName("Aircraft 1");
        aircraftCreateRepresentation.setAircraftCapacity(100);
        aircraftCreateRepresentation.setAircraftColumns(6);
        aircraftCreateRepresentation.setAircraftRows(6);
        return aircraftCreateRepresentation;
    }

    public static Aircraft createAircraft(){
        return new Aircraft(createAircraftCreateRepresentation(), AirlineUtil.createAirline());
    }

    public static Aircraft createAircraft(Airline airline){
        return new Aircraft(createAircraftCreateRepresentation(), airline);
    }

    public static AircraftRepresentation createAircraftRepresentation(){
        AircraftRepresentation aircraftRepresentation = new AircraftRepresentation();
        aircraftRepresentation.setId(1);
        aircraftRepresentation.setAircraftName("Aircraft 1");
        aircraftRepresentation.setAircraftCapacity(200);
        return aircraftRepresentation;
    }
}
