package org.acme.domain;

import jakarta.persistence.*;
import org.acme.constant.Role;
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

    @Column(name = "email", nullable = false, length = 30)
    private String email;

    @Column(name = "phoneNumber", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "role", nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    public User() {
    }

    public User(UserCreateRepresentation userCreateRepresentation, Role role) {
        this.username = userCreateRepresentation.getUsername();
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

    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void updateDetails(UserUpdateRepresentation userUpdateRepresentation){
        this.email = userUpdateRepresentation.getEmail();
        this.phoneNumber = userUpdateRepresentation.getPhoneNumber();
    }
}
