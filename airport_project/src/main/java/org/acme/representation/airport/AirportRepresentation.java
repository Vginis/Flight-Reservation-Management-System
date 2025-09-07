package org.acme.representation.airport;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class AirportRepresentation {

    public Integer airportId;
    public String airportName;
    public String city;
    public String country;
    public String u3digitCode;
    public List<Integer> depFlights;
    public List<Integer> arrFlights;

}