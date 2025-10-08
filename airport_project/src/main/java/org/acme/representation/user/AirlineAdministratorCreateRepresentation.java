package org.acme.representation.user;

import jakarta.validation.constraints.Pattern;
import org.acme.representation.AddressCreateRepresentation;

import java.util.List;

public class AirlineAdministratorCreateRepresentation extends UserCreateRepresentation{
    @Pattern(
            regexp = "^[A-Z]{2,3}$",
            message = "Airline code must have only uppercase letters (e.g. AG, ACA)"
    )
    private String airlineU2digitCode;

    public AirlineAdministratorCreateRepresentation() {
    }

    public AirlineAdministratorCreateRepresentation(String username, String firstName, String lastName, String email, String phoneNumber, List<AddressCreateRepresentation> addresses, String airlineU2digitCode) {
        super(username, firstName, lastName, email, phoneNumber, addresses);
        this.airlineU2digitCode = airlineU2digitCode;
    }

    public String getAirlineU2digitCode() {
        return airlineU2digitCode;
    }

    public void setAirlineU2digitCode(String airlineU2digitCode) {
        this.airlineU2digitCode = airlineU2digitCode;
    }
}
