package org.acme.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "airports")
public class Airport {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "name", nullable = false, length = 30, unique = true)
    private String airportName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @Column(name = "u3digitCode", nullable = false, length = 3, unique = true)
    private String u3digitCode;

    @OneToMany(mappedBy = "departureAirport", cascade = CascadeType.REMOVE)
    private List<Flight> depFlights = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalAirport", cascade = CascadeType.REMOVE)
    private List<Flight> arrFlights = new ArrayList<>();

    public Airport() {
    }

    public Airport(String airportName, City city, Country country, String u3digitCode) {
        this.airportName = airportName;
        this.city = city;
        this.country = country;
        this.u3digitCode = u3digitCode;
        this.arrFlights = new ArrayList<>();
        this.depFlights = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getU3digitCode() {
        return u3digitCode;
    }

    public City getCity() {
        return city;
    }

    public Country getCountry() {
        return country;
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
        return Objects.equals(id, airport.id) && Objects.equals(airportName, airport.airportName) && Objects.equals(city, airport.city) && Objects.equals(country, airport.country) && Objects.equals(u3digitCode, airport.u3digitCode);
    }

    public void update(String airportName,City city,Country country){
        this.airportName = airportName;
        this.city = city;
        this.country = country;
    }
}
