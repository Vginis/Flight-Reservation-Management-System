package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class PassengerRepresentation {
    public Integer id;
    public String username;
    public String password;
    public String email;
    public String phoneNum;
    public String passport_id;
    public List<Integer> reservationsId;

}
