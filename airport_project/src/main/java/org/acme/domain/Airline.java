package org.acme.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Airline")
public class Airline extends AccountManagement {

    @Column(name = "name", nullable = true, length = 30)
    public String name;

    @Column(name = "2digit_id", nullable = true, length = 2)
    public String u2digit_id;

    public Airline() {
    }

    public Airline(String name, String u2digit_id, String username, String password) {
        super(username, password);
        this.name = name;
        this.u2digit_id = u2digit_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getU2digit_id() {
        return u2digit_id;
    }

    public void setU2digit_id(String u2digit_id) {
        this.u2digit_id = u2digit_id;
    }

}
