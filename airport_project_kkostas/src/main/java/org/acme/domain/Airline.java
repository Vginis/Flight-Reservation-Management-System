package org.acme.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Airline")
public class Airline extends AccountManagement {

    @Column(name = "airline_name", nullable = false, length = 20)
    public String airline_name;

    @Column(name = "two_digit_id", nullable = false, length = 2)
    public String two_digit_id;

    public Airline() {
    }

    public Airline(String airline_name, String two_digit_id, String username, String password) {
        super(username, password);
        this.airline_name = airline_name;
        this.two_digit_id = two_digit_id;
    }

    public void setAirline_name(String airline_name) {
        this.airline_name = airline_name;
    }

    public String getAirline_name() {
        return airline_name;
    }

    public void setTwo_digit_id(String two_digit_id) {
        this.two_digit_id = two_digit_id;
    }

    public String getTwo_digit_id() {
        return two_digit_id;
    }
}
