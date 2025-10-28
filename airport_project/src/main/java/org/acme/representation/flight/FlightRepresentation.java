package org.acme.representation.flight;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class FlightRepresentation {

    private Integer id;
    private String flightNumber;
    private String flightUUID;
    private String airlineU2DigitCode;
    private String departureAirport;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime departureTime;
    private String arrivalAirport;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime arrivalTime;
    private String flightState;
    private Integer aircraftId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightUUID() {
        return flightUUID;
    }

    public void setFlightUUID(String flightUUID) {
        this.flightUUID = flightUUID;
    }

    public String getAirlineU2DigitCode() {
        return airlineU2DigitCode;
    }

    public void setAirlineU2DigitCode(String airlineU2DigitCode) {
        this.airlineU2DigitCode = airlineU2DigitCode;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getFlightState() {
        return flightState;
    }

    public void setFlightState(String flightState) {
        this.flightState = flightState;
    }

    public Integer getAircraftId() {
        return aircraftId;
    }

    public void setAircraftId(Integer aircraftId) {
        this.aircraftId = aircraftId;
    }
}
