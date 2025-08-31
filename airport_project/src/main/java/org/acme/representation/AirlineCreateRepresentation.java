package org.acme.representation;

import jakarta.validation.constraints.NotBlank;

public class AirlineCreateRepresentation {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String airlineName;
    @NotBlank
    private String u2digitCode;

    public AirlineCreateRepresentation(String username, String password, String airlineName, String u2digitCode) {
        this.username = username;
        this.password = password;
        this.airlineName = airlineName;
        this.u2digitCode = u2digitCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
