package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class PassengerRepresentation {
    public String username;
    public String password;
    public Integer id;
    public String email;
    public String phoneNumber;
    public String passportId;
}
