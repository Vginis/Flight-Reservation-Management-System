package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AirlineRepresentation {
    public Integer airlineId;
    public String name;
    public String u2digitCode;

}
