package org.acme.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Administrator")
public class Administrator extends AccountManagement {

    @Column(name = "admin_id", nullable = false, length = 20)
    public String admin_id;

    public Administrator() {
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
}
