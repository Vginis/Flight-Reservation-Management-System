package org.acme.domain;

import jakarta.persistence.*;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.airline.AirlineUpdateRepresentation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "airline")
public class Airline {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "name", length = 30, unique = true)
    private String airlineName;

    @Column(name = "u2digitCode", length = 3, unique = true)
    private String u2digitCode;

    @OneToMany(mappedBy = "airline", cascade = CascadeType.REMOVE)
    private List<Flight> flights = new ArrayList<>();

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
    private List<AirlineAdministrator> administrators = new ArrayList<>();

    @OneToMany(mappedBy = "airline", cascade = CascadeType.ALL)
    private List<Aircraft> aircrafts = new ArrayList<>();

    public Airline() {
        super();
    }

    public Airline(AirlineCreateRepresentation airlineCreateRepresentation) {
        this.airlineName = airlineCreateRepresentation.getAirlineName();
        this.u2digitCode = airlineCreateRepresentation.getU2digitCode();
        this.flights = new ArrayList<>();
        this.administrators = new ArrayList<>();
        this.aircrafts = new ArrayList<>();
    }

    public Airline(String airlineName, String u2digitCode) {
        this.airlineName = airlineName;
        this.u2digitCode = u2digitCode;
        this.flights = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String name) {
        this.airlineName = name;
    }

    public String getU2digitCode() {
        return u2digitCode;
    }

    public void setU2digitCode(String u2digitCode) {
        this.u2digitCode = u2digitCode;
    }

    public List<AirlineAdministrator> getAdministrators() {
        return administrators;
    }

    public void setAdministrators(List<AirlineAdministrator> administrators) {
        this.administrators = administrators;
    }

    public List<Aircraft> getAircrafts() {
        return aircrafts;
    }

    public void setAircrafts(List<Aircraft> aircrafts) {
        this.aircrafts = aircrafts;
    }

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
        return (mostVisitedAirport != null) ? mostVisitedAirport.getAirportName() : null;
    }

    public Double completeness() {
        double sum = 0;
        int count = 0;
        double average;
        for (Flight flight : flights){
            count += 1;
            //ToDO Fix sum += (double)flight.getAvailableSeats()/flight.getAircraftCapacity();
        }
        average = 100-(sum/count)*100;
        return average;
    }

    public void updateAirlineDetails(AirlineUpdateRepresentation representation){
        this.airlineName = representation.getAirlineName();
        this.u2digitCode = representation.getU2digitCode();
    }
}
