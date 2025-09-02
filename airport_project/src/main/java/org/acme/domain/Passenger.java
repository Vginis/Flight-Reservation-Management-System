package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Passenger extends User {
    @Column(name = "passport_id", length = 20, unique = true)
    private String passport_id;

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

    public String getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(String passport_id) {
        this.passport_id = passport_id;
    }

    public void addReservation(Reservation reservation) {
            if (reservation == null)
                return;
            if (reservation.getPassenger() != this)
                throw new RuntimeException("This reservation is not for this passenger");
            if (reservations.contains(reservation))
                throw new RuntimeException("Reservation already exists.");
            reservations.add(reservation);
        }

    public void removeReservation(Reservation reservation) {
        if (reservation == null)
            return;
        if (!reservations.contains(reservation))
            throw new RuntimeException("Reservation does not exist.");
        reservations.remove(reservation);
    }

    public boolean isValidEmail(String Email) {
        String emailRegex = "^.{3,20}@(unipi\\.gr|outlook\\.com|aueb\\.gr|gmail\\.com)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

    public void clearReservations() {
        this.reservations.clear();
    }

}
