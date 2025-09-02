package org.acme.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable
public class FlightInformation {
    @Column(name = "flight_number", length = 20)
    private String flightNumber;
    @Column(name = "flight_uuid", length = 20)
    private UUID flightUUID;
    @Column(name = "ticket_price", nullable = false)
    private String ticketPrice;
    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    public FlightInformation() {
    }

    public FlightInformation(String flightNumber, UUID flightUUID, String ticketPrice, String seatNumber) {
        this.flightNumber = flightNumber;
        this.flightUUID = flightUUID;
        this.ticketPrice = ticketPrice;
        this.seatNumber = seatNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public UUID getFlightUUID() {
        return flightUUID;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }
}
