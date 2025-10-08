package org.acme.representation.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.acme.representation.AddressCreateRepresentation;

import java.util.List;

public class UserCreateRepresentation{
    @NotBlank
    private String username;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email(message = "Email should be a valid email address.")
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;
    @NotNull
    private List<AddressCreateRepresentation> addresses;

    public UserCreateRepresentation() {
    }

    public UserCreateRepresentation(String username, String firstName, String lastName, String email, String phoneNumber, List<AddressCreateRepresentation> addresses) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.addresses = addresses;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<AddressCreateRepresentation> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressCreateRepresentation> addresses) {
        this.addresses = addresses;
    }
}
