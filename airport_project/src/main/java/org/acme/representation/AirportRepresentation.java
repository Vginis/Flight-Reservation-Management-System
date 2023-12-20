package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AirportRepresentation {
    public Integer airportId;
    public String name;
    public String city;
    public String country;
    public String u3digitCode;
}