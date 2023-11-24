package org.acme.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Administrator")
public class Administrator extends AccountManagement {
    //TODO NA SKEFTOYME AN THA TO VGALOYME
    @Column(name = "admin_id", nullable = true, length = 20)
    public String admin_id;

    public Administrator() {
        this.setUsername("admin");
        this.setPassword("JeandDig1@");
        this.admin_id="";
    }

    public Administrator(String admin_id, String username, String password) {
        super(username, password);
        this.admin_id = admin_id;
    }

    public String getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(String admin_id) {
        this.admin_id = admin_id;
    }

    //TODO addAirports(), removeAirline,User,Airports()
}