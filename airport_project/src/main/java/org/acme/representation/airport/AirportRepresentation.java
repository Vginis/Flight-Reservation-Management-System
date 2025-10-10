package org.acme.representation.airport;

public class AirportRepresentation {

    private Integer airportId;
    private String airportName;
    private String city;
    private String country;
    private String u3digitCode;

    public Integer getAirportId() {
        return airportId;
    }

    public void setAirportId(Integer airportId) {
        this.airportId = airportId;
    }

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