package org.acme.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("AIRLINE")
public class Airline extends AccountManagement {

    @Column(name = "name", nullable = true, length = 30)
    private String name;

    @Column(name = "u2digitCode", nullable = true, length = 2)
    private String u2digitCode;

    public Airline() {
    }

    public Airline(String name, String u2digitCode, String username, String password) {
        super(username, password);
        this.name = name;
        this.u2digitCode = u2digitCode;
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

    // TODO add-removeFlight, popular_airport(),completenessxiixi()
}
