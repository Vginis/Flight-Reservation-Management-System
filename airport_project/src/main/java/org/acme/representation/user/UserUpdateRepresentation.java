package org.acme.representation.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.acme.representation.AddressCreateRepresentation;

import java.util.List;

public class UserUpdateRepresentation {
    @Email(message = "Email should be a valid email address.")
    private String email;
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;
    @NotNull
    private List<AddressCreateRepresentation> addresses;

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
