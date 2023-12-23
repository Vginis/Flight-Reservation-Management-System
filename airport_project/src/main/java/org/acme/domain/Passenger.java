package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

@Entity
@DiscriminatorValue("PASSENGER")
public class Passenger extends AccountManagement {

    @Column(name = "email", nullable = true, length = 20)
    private String email;

    @Column(name = "phoneNum", length = 20)
    private String phoneNum;

    @Column(name = "passport_id", length = 20, unique = true)
    private String passport_id;

    @OneToMany(mappedBy ="passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public Passenger(){
    }

    public Passenger(String email, String phoneNum, String passport_id, String username, String password){
        super(username, password);
        this.setEmail(email);
        this.phoneNum = phoneNum;
        this.passport_id = passport_id;
        this.reservations = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        if (isValidEmail(email))
            this.email = email;
        else
            throw new RuntimeException("Invalid email address");
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(String passport_id) {
        this.passport_id = passport_id;
    }

    public void addReservation(Reservation reservation){
            if (reservation == null)
                return;
            if (reservation.getPassenger()!=this)
                throw new RuntimeException("This reservation is not for this passenger");
            if (reservations.contains(reservation))
                throw new RuntimeException("Reservation already exists.");
            reservations.add(reservation);
        }

    public void removeReservation(Reservation reservation){
        if (reservation == null)
            return;
        if (!reservations.contains(reservation))
            throw new RuntimeException("Reservation does not exist.");
        reservations.remove(reservation);
    }

    public boolean isValidEmail(String Email){
        String emailRegex = "^.{3,20}@(unipi\\.gr|outlook\\.com|aueb\\.gr|gmail\\.com)$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(Email);
        return matcher.matches();
    }

}
