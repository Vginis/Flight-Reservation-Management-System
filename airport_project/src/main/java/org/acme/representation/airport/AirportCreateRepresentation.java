package org.acme.representation.airport;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
//TODO Rename this dto to CreateUpdateRepresentation
public class AirportCreateRepresentation {
    @NotBlank
    private String airportName;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
    @Pattern(
            regexp = "^[A-Z]{3}$",
            message = "Airport code must be exactly 3 uppercase letters (e.g. SKG, ATH, LHR)"
    )
    private String u3digitCode;

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
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

    public String getU3digitCode() {
        return u3digitCode;
    }

    public void setU3digitCode(String u3digitCode) {
        this.u3digitCode = u3digitCode;
    }
}
