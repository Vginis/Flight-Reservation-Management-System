package org.acme.representation.flight;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.acme.representation.airport.AirportRepresentation;

import java.time.LocalDateTime;

public class FlightRepresentation {

    private Integer id;
    private String flightNumber;
    private String flightUUID;
    private String flightStatus;
    private String airlineU2DigitCode;
    private AirportRepresentation departureAirport;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime departureTime;
    private AirportRepresentation arrivalAirport;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime arrivalTime;
    private AircraftRepresentation aircraft;

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

    public String getFlightStatus() {
        return flightStatus;
    }

    public void setFlightStatus(String flightStatus) {
        this.flightStatus = flightStatus;
    }

    public String getAirlineU2DigitCode() {
        return airlineU2DigitCode;
    }

    public void setAirlineU2DigitCode(String airlineU2DigitCode) {
        this.airlineU2DigitCode = airlineU2DigitCode;
    }

    public AirportRepresentation getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(AirportRepresentation departureAirport) {
        this.departureAirport = departureAirport;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public AirportRepresentation getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(AirportRepresentation arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public AircraftRepresentation getAircraft() {
        return aircraft;
    }

    public void setAircraft(AircraftRepresentation aircraft) {
        this.aircraft = aircraft;
    }
}
