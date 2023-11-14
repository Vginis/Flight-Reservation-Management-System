package org.acme.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "Airports")
public class Airport {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name="name", nullable = false, length = 30)
    private String name;

    @Column(name="city", nullable = false, length = 20)
    private String city;

    @Column(name="country", nullable = false, length = 20)
    private String country;

    @Column(name="u3digitCode", nullable = false, length = 3)
    private String u3digitCode;

    public Airport(){
    }

    public Airport(String name, String city, String country, String u3digitCode){
        this.name = name;
        this.city = city;
        this.country = country;
        this.u3digitCode = u3digitCode;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return Objects.equals(id, airport.id) && Objects.equals(name, airport.name) && Objects.equals(city, airport.city)
                && Objects.equals(country, airport.country);
    }
}
