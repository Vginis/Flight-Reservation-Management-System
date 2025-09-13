package org.acme.representation;

public class AddressRepresentation {
    private String addressName;
    private String city;
    private String country;

    public AddressRepresentation() {
    }

    public AddressRepresentation(String addressName, String city, String country) {
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
