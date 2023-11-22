package org.acme.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("AIRLINE")
public class Airline extends AccountManagement {

    @Column(name = "name", nullable = true, length = 30)
    private String name;

    @Column(name = "u2digitCode", nullable = true, length = 2)
    private String u2digitCode;

    @OneToMany(mappedBy = "airline")
    private List<Flight> flights = new ArrayList<>();

    public Airline() {
        this.name = "";
        this.u2digitCode = "";
        this.setUsername("");
        this.setPassword("");
        this.flights = new ArrayList<>();
    }

    public Airline(String name, String u2digitCode, String username, String password) {
        super(username, password);
        this.name = name;
        this.u2digitCode = u2digitCode;
        this.flights = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getU2digitCode() {
        return u2digitCode;
    }

    public void setU2digitCode(String u2digitCode) {
        this.u2digitCode = u2digitCode;
    }

    // TODO add-removeFlight(done), popular_airport(done),completenessxiixi()
    public void addFlight(Flight flight) {
        if (flight == null)
            return;
        if (flight.getAirline() != this)
            throw new RuntimeException("Entry in the wrong Airline.");
        if (flights.contains(flight)) {
            throw new RuntimeException("Flight already exists.");
        }
        flights.add(flight);
    }

    public void removeFlight(Flight flight) {
        if (flight == null)
            return;
        if (!flights.contains(flight))
            throw new RuntimeException("Flight doesn't exist.");
        flights.remove(flight);
    }

    public String mostPopularAirport() {
        Map<Airport, Integer> airportCounts = new HashMap<>();

        for (Flight flight : flights) {
            Airport departureAirport = flight.getDepartureAirport();
            Airport arrivalAirport = flight.getArrivalAirport();


            airportCounts.put(departureAirport, airportCounts.getOrDefault(departureAirport, 0) + 1);
            airportCounts.put(arrivalAirport, airportCounts.getOrDefault(arrivalAirport, 0) + 1);
        }

        int maxCount = 0;
        Airport mostVisitedAirport = null;

        for (Map.Entry<Airport, Integer> entry : airportCounts.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostVisitedAirport = entry.getKey();
            }
        }

        return (mostVisitedAirport != null) ? mostVisitedAirport.getName() : null;
    }
    }

