package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Passenger extends User {
    @Column(name = "passport_id", length = 20, unique = true)
    private String passportId;

    @OneToMany(mappedBy ="passenger", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reservation> reservations = new ArrayList<>();

    public Passenger(){
        super();
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }


    public String getPassportId() {
        return passportId;
    }

    public void clearReservations() {
        this.reservations.clear();
    }

}
