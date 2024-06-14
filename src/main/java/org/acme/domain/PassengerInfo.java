package org.acme.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class PassengerInfo {

    @Column(name = "firstName", nullable = false, length = 30)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 30)
    private String lastName;

    @Column(name = "passportId", nullable = false, length = 10)
    private  String passportId;

    public PassengerInfo() {
    }

    public PassengerInfo(String firstName, String lastName, String passportId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

}
