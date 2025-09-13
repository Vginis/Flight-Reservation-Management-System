package org.acme.representation;

import jakarta.validation.constraints.NotBlank;

public class AddressCreateRepresentation {
    @NotBlank
    private String addressName;
    @NotBlank
    private String city;
    @NotBlank
    private String country;

    public AddressCreateRepresentation() {
    }

    public AddressCreateRepresentation(String addressName, String city, String country) {
        this.addressName = addressName;
        this.city = city;
        this.country = country;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
