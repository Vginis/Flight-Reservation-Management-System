package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class AirlineRepresentation{
    public Integer id;
    public String name;
    public String u2digitCode;
    public String username;
    public String password;

}
