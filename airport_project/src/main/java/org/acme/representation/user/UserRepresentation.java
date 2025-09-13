package org.acme.representation.user;

import org.acme.representation.AddressRepresentation;

import java.util.List;

public class UserRepresentation {
    private Integer id;
    private String username;
    private String email;
    private String phoneNumber;
    private String role;
    private List<AddressRepresentation> addresses;

    public UserRepresentation() {
    }

    public UserRepresentation(Integer id, String username, String email, String phoneNumber, String role, List<AddressRepresentation> addresses) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.addresses = addresses;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<AddressRepresentation> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressRepresentation> addresses) {
        this.addresses = addresses;
    }
}
