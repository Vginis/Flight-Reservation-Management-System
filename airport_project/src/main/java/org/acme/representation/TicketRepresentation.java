package org.acme.representation;

import org.acme.representation.flight.FlightRepresentation;

import java.util.List;

public class TicketRepresentation {
    private String ticketUUID;
    private List<Integer> luggageWeights;
    private String firstName;
    private String lastName;
    private String passportId;
    private FlightRepresentation flightRepresentation;

    public String getTicketUUID() {
        return ticketUUID;
    }

    public void setTicketUUID(String ticketUUID) {
        this.ticketUUID = ticketUUID;
    }

    public List<Integer> getLuggageWeights() {
        return luggageWeights;
    }

    public void setLuggageWeights(List<Integer> luggageWeights) {
        this.luggageWeights = luggageWeights;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassportId() {
        return passportId;
    }

    public void setPassportId(String passportId) {
        this.passportId = passportId;
    }

    public FlightRepresentation getFlightRepresentation() {
        return flightRepresentation;
    }

    public void setFlightRepresentation(FlightRepresentation flightRepresentation) {
        this.flightRepresentation = flightRepresentation;
    }
}
