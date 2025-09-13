package org.acme.domain;

import jakarta.persistence.*;
import org.acme.constant.Role;
import org.acme.representation.user.PassengerCreateRepresentation;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passengers")
@PrimaryKeyJoinColumn(name = "user_id")
public class Passenger extends User {
    @Column(name = "passport", length = 20, unique = true)
    private String passport;

    @OneToMany(mappedBy ="passenger", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();

    public Passenger(){
        super();
    }

    public Passenger(PassengerCreateRepresentation passengerCreateRepresentation){
        super(passengerCreateRepresentation, Role.PASSENGER);
        this.passport = passengerCreateRepresentation.getPassport();
        this.reservations = new ArrayList<>();
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }


    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public void clearReservations() {
        this.reservations.clear();
    }

}
