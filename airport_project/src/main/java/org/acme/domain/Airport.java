package org.acme.domain;

import jakarta.persistence.*;
import jakarta.xml.ws.WebFault;

import java.util.Objects;

@Entity
@Table(name = "Airport")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type" , discriminatorType = DiscriminatorType.STRING)
public class Airport {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;
    @Column(name="name",nullable = false,length = 30)
    private String Name;
    @Column(name="city",nullable = false,length = 20)
    private String City;
    @Column(name="country",nullable = false,length = 20)
    private String Country;
    @Column(name="3digitCode",nullable = false,length = 3)
    private String U3Digit_code;

    public Airport(){

    }

    public Airport(String name,String city,String country,String threeDigitCode){
        this.Name = name;
        this.City = city;
        this.Country = country;
        this.U3Digit_code = threeDigitCode;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getU3Digit_code() {
        return U3Digit_code;
    }

    public void setU3Digit_code(String u3Digit_code) {
        U3Digit_code = u3Digit_code;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(id, airport.id) && Objects.equals(Name, airport.Name) && Objects.equals(City, airport.City)
                && Objects.equals(Country, airport.Country);
    }
}
