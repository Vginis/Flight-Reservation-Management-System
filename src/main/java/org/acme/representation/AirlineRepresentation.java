package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class AirlineRepresentation{
    public Integer id;
    public String username;
    public String password;
    public String airlineName;
    public String u2digitCode;
    public List<FlightRepresentation> flights;
}
