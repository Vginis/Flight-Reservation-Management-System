package org.acme.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "airline_administrator")
@PrimaryKeyJoinColumn(name = "user_id")
public class AirlineAdministrator extends User {
    @JoinColumn(name = "airline_admin_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Airline airline;

    public AirlineAdministrator() {
    }

    public AirlineAdministrator(Airline airline) {
        this.airline = airline;
    }

    public Airline getAirline() {
        return airline;
    }
}
