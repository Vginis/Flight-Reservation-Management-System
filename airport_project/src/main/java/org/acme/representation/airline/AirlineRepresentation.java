package org.acme.representation.airline;

import org.acme.representation.FlightRepresentation;

import java.util.List;

public class AirlineRepresentation{
    private Integer id;
    private String username;
    private String password;
    private String airlineName;
    private String u2digitCode;
    private List<FlightRepresentation> flights;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getU2digitCode() {
        return u2digitCode;
    }

    public void setU2digitCode(String u2digitCode) {
        this.u2digitCode = u2digitCode;
    }

    public List<FlightRepresentation> getFlights() {
        return flights;
    }

    public void setFlights(List<FlightRepresentation> flights) {
        this.flights = flights;
    }
}
