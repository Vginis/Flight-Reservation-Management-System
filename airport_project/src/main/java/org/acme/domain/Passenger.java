package org.acme.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import org.acme.constant.Role;
import org.acme.representation.user.PassengerCreateRepresentation;

@Entity
@Table(name = "passengers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Passenger extends User {
    @Column(name = "passport", length = 20, unique = true)
    private String passport;

    public Passenger(){
        super();
    }

    public Passenger(PassengerCreateRepresentation passengerCreateRepresentation){
        super(passengerCreateRepresentation, Role.PASSENGER);
        this.passport = passengerCreateRepresentation.getPassport();
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

}
