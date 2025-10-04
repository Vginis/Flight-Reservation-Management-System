package org.acme.representation.flight;

public class FlightMultipleParamsSearchDTO {
    private String departureAirport;
    private String departureDate;
    private String arrivalAirport;
    private String arrivalDate;

    public FlightMultipleParamsSearchDTO() {
    }

    public FlightMultipleParamsSearchDTO(String departureAirport, String departureDate, String arrivalAirport, String arrivalDate) {
        this.departureAirport = departureAirport;
        this.departureDate = departureDate;
        this.arrivalAirport = arrivalAirport;
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }
}
