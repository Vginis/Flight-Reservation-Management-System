package org.acme.representation.user;

import org.acme.representation.AddressCreateRepresentation;
import org.acme.representation.airline.AirlineCreateRepresentation;

import java.util.List;

public class AirlineAdministratorCreateRepresentation extends UserCreateRepresentation{

    private AirlineCreateRepresentation airlineCreateRepresentation;

    public AirlineAdministratorCreateRepresentation() {
    }

    public AirlineAdministratorCreateRepresentation(String username, String firstName, String lastName, String email,
                                                    String phoneNumber, List<AddressCreateRepresentation> addresses,
                                                    AirlineCreateRepresentation airlineCreateRepresentation) {
        super(username, firstName, lastName, email, phoneNumber, addresses);
        this.airlineCreateRepresentation = airlineCreateRepresentation;
    }

    public AirlineCreateRepresentation getAirlineCreateRepresentation() {
        return airlineCreateRepresentation;
    }

    public void setAirlineCreateRepresentation(AirlineCreateRepresentation airlineCreateRepresentation) {
        this.airlineCreateRepresentation = airlineCreateRepresentation;
    }
}
