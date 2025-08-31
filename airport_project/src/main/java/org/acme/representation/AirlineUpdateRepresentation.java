package org.acme.representation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AirlineUpdateRepresentation {
    @NotNull
    private Integer id;
    @NotBlank
    private String airlineName;
    @NotBlank
    private String u2digitCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
