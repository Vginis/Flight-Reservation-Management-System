package org.acme.representation.passenger;

import org.acme.representation.user.UserUpdateRepresentation;

public class PassengerUpdateRepresentation extends UserUpdateRepresentation {
    private String passport;

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }
}
