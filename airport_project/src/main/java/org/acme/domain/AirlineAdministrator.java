package org.acme.domain;

import jakarta.persistence.*;
import org.acme.constant.Role;
import org.acme.representation.user.UserCreateRepresentation;

@Entity
@Table(name = "airline_administrator")
@PrimaryKeyJoinColumn(name = "user_id")
public class AirlineAdministrator extends User {
    @JoinColumn(name = "airline_admin_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Airline airline;

    public AirlineAdministrator() {
    }

    public AirlineAdministrator(UserCreateRepresentation userCreateRepresentation, Airline airline) {
        super(userCreateRepresentation, Role.AIRLINE_ADMINISTRATOR);
        this.airline = airline;
    }

    public Airline getAirline() {
        return airline;
    }
}
