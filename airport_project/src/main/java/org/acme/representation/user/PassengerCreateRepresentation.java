package org.acme.representation.user;

import jakarta.validation.constraints.NotBlank;
import org.acme.representation.AddressCreateRepresentation;

import java.util.List;

public class PassengerCreateRepresentation extends UserCreateRepresentation{
    @NotBlank private String passport;

    public PassengerCreateRepresentation() {
    }

    public PassengerCreateRepresentation(String username, String email, String phoneNumber, List<AddressCreateRepresentation> addresses, String passport) {
        super(username, email, phoneNumber, addresses);
        this.passport = passport;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
