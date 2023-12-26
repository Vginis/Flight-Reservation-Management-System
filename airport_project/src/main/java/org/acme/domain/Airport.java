package org.acme.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Airports")
public class Airport {

    @Id
    @Column(name="airportId", length = 10)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer airportId;

    @Column(name="name", nullable = false, length = 30, unique = true)
    private String airportName;

    @Column(name="city", nullable = false, length = 20)
    private String city;

    @Column(name="country", nullable = false, length = 20)
    private String country;

    @Column(name="u3digitCode", nullable = false, length = 3, unique = true)
    private String u3digitCode;

    public Airport(){}

    public Airport(String airportName, String city, String country, String u3digitCode){
        this.airportName = airportName;
        this.city = city;
        this.country = country;
        this.u3digitCode = u3digitCode;
    }

    public Integer getAirportId() {
        return airportId;
    }

    public void setAirportId(Integer airportId) {this.airportId = airportId;}

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String name) {
        this.airportName = name;
    }

    public String getU3digitCode() {
        return u3digitCode;
    }

    public void setU3digitCode(String u3digitCode) {
        this.u3digitCode = u3digitCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(airportId, airport.airportId) && Objects.equals(airportName, airport.airportName) && Objects.equals(city, airport.city)
                && Objects.equals(country, airport.country) && Objects.equals(u3digitCode, airport.u3digitCode);
    }

}
