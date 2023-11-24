package org.acme.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Administrator")
public class Administrator extends AccountManagement {

    @Column(name = "admin_id", nullable = true, length = 20)
    public String admin_id;

    @OneToMany(mappedBy = "airportId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    public List<Airport> airportsList;

    @OneToMany(mappedBy = "u2digitCode", cascade = CascadeType.REMOVE, orphanRemoval = true)
    public List<Airline> airlinesList;

    @OneToMany(mappedBy = "passport_id", cascade = CascadeType.REMOVE, orphanRemoval = true)
    public List<Passenger> passengersList;

    public Administrator() {
        this.setUsername("");
        this.setPassword("Adfkmfi2@");
        this.admin_id = "";
        this.airportsList = new ArrayList<>();
        this.airlinesList = new ArrayList<>();
        this.passengersList = new ArrayList<>();
    }

    public Administrator(String admin_id, String username, String password) {
        super(username, password);
        this.admin_id = admin_id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    public void addAirport(Airport airport) {
        if (airport == null) return;
        if (this.airportsList.contains(airport)) {
            throw new RuntimeException("Airport already exists.");
        }
        airportsList.add(airport);
    }

    public void removeAirport(Airport airport) {
        if (airport == null) return;
        if (!this.airportsList.contains(airport)) {
            throw new RuntimeException("Airport does not exist");
        }
        airportsList.remove(airport);
    }

    public void addAirline(Airline airline) {
        if (airline == null) return;
        if (this.airlinesList.contains(airline)) {
            throw new RuntimeException("Airline already exists.");
        }
        airlinesList.add(airline);
    }

    public void removeAirline(Airline airline) {
        if (airline == null) return;
        if (!this.airlinesList.contains(airline)) {
            throw new RuntimeException("Airline does not exist");
        }
        airlinesList.remove(airline);

    }

    public void addPassenger(Passenger passenger) {
        if (passenger == null) return;
        if (this.passengersList.contains(passenger)) {
            throw new RuntimeException("Passenger already exists.");
        }
        passengersList.add(passenger);
    }

    public void removePassenger(Passenger passenger) {
        if (passenger == null) return;
        if (!this.passengersList.contains(passenger)) {
            throw new RuntimeException("Passenger does not exist");
        }
        passengersList.remove(passenger);
    }

}
