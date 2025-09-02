package org.acme.domain;

import jakarta.persistence.*;

import java.util.Objects;
import java.util.regex.*;


@Entity
@Table(name = "User")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type" , discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Integer id;

    @Column(name = "username", nullable = false, length = 20, unique = true)
    private String username;

    @Column(name = "email", nullable = false, length = 20)
    private String email;

    @Column(name = "phoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "role", nullable = false, length = 20)
    private String role;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer newId) {
        this.id = newId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
