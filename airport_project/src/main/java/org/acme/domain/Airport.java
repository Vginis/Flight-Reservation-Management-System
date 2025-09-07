package org.acme.domain;

import jakarta.persistence.*;
import org.acme.representation.airport.AirportCreateRepresentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @Column(name = "airportId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer airportId;

    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String airportName;

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "country", nullable = false, length = 20)
    private String country;

    @Column(name = "u3digitCode", nullable = false, length = 3, unique = true)
    private String u3digitCode;

    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.REMOVE)
    private List<Flight> depFlights = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.REMOVE)
    private List<Flight> arrFlights = new ArrayList<>();

    public Airport() {
    }

    public Airport(String airportName, String city, String country, String u3digitCode) {
        this.airportName = airportName;
        this.city = city;
        this.country = country;
        this.u3digitCode = u3digitCode;
        this.arrFlights = new ArrayList<>();
        this.depFlights = new ArrayList<>();
    }

    public Integer getAirportId() {
        return airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getU3digitCode() {
        return u3digitCode;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public List<Flight> getDepFlights() {
        return depFlights;
    }

    public void setDepFlights(List<Flight> depFlights) {
        this.depFlights = depFlights;
    }

    public List<Flight> getArrFlights() {
        return arrFlights;
    }

    public void setArrFlights(List<Flight> arrFlights) {
        this.arrFlights = arrFlights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(airportId, airport.airportId) && Objects.equals(airportName, airport.airportName) && Objects.equals(city, airport.city) && Objects.equals(country, airport.country) && Objects.equals(u3digitCode, airport.u3digitCode);
    }

    public void update(AirportCreateRepresentation representation){
        this.airportName = representation.getAirportName();
        this.city = representation.getCity();
        this.country = representation.getCountry();
    }
}
