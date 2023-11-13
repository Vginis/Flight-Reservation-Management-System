package org.acme.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Passenger")
public class Passenger extends AccountManagement {

    @Column(name = "email", nullable = true, length = 20)
    public String email;

    @Column(name = "phoneNum", nullable = true, length = 20)
    public String phoneNum;

    @Column(name = "passport_id", nullable = true, length = 20)
    public String passport_id;

    public Passenger() {
    }

    public Passenger(String email, String phoneNum, String passport_id, String username, String password) {
        super(username, password);
        this.email = email;
        this.phoneNum = phoneNum;
        this.passport_id = passport_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassport_id() {
        return passport_id;
    }

    public void setPassport_id(String passport_id) {
        this.passport_id = passport_id;
    }

}
