package org.acme.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class FlightInformation {
    @Column(name = "flight_number", length = 20)
    private String flightNumber;
    @Column(name = "flight_uuid")
    private String flightUUID;

    public FlightInformation() {
    }

    public FlightInformation(String flightNumber, String flightUUID) {
        this.flightNumber = flightNumber;
        this.flightUUID = flightUUID;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public String getFlightUUID() {
        return flightUUID;
    }

}
