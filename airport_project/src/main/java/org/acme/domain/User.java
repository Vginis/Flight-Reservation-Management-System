package org.acme.domain;

import jakarta.persistence.*;
import org.acme.representation.user.UserCreateRepresentation;
import org.acme.representation.user.UserUpdateRepresentation;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "type" , discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(name = "username", nullable = false, length = 30, unique = true)
    private String username;

    @Column(name = "first_name", nullable = false, length = 50, unique = true)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50, unique = true)
    private String lastName;

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "phoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "role", nullable = false, length = 30)
    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public User() {
    }

    public User(UserCreateRepresentation userCreateRepresentation, String role) {
        this.username = userCreateRepresentation.getUsername();
        this.firstName = userCreateRepresentation.getFirstName();
        this.lastName = userCreateRepresentation.getLastName();
        this.email = userCreateRepresentation.getEmail();
        this.phoneNumber = userCreateRepresentation.getEmail();
        this.role = role;
        this.addresses = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void updateDetails(UserUpdateRepresentation userUpdateRepresentation){
        this.firstName = userUpdateRepresentation.getFirstName();
        this.lastName = userUpdateRepresentation.getLastName();
        this.email = userUpdateRepresentation.getEmail();
        this.phoneNumber = userUpdateRepresentation.getPhoneNumber();
    }
}
