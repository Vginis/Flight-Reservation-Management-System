package org.acme.representation.airline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AirlineCreateRepresentation {

    @NotBlank
    private String airlineName;
    @Pattern(
            regexp = "^[A-Z]{2,3}$",
            message = "Airline code must have only uppercase letters (e.g. AG, ACA)"
    )
    private String u2digitCode;

    public AirlineCreateRepresentation() {
    }

    public AirlineCreateRepresentation(String airlineName, String u2digitCode) {
        this.airlineName = airlineName;
        this.u2digitCode = u2digitCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getU2digitCode() {
        return u2digitCode;
    }

    public void setU2digitCode(String u2digitCode) {
        this.u2digitCode = u2digitCode;
    }
}
