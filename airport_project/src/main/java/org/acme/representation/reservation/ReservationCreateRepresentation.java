package org.acme.representation.reservation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReservationCreateRepresentation {
    @NotBlank
    private String flightUUID;
    @NotNull
    @Valid
    private List<TicketCreateRepresentation> ticketCreateRepresentationList;

    public String getFlightUUID() {
        return flightUUID;
    }

    public void setFlightUUID(String flightUUID) {
        this.flightUUID = flightUUID;
    }

    public List<TicketCreateRepresentation> getTicketCreateRepresentationList() {
        return ticketCreateRepresentationList;
    }

    public void setTicketCreateRepresentationList(List<TicketCreateRepresentation> ticketCreateRepresentationList) {
        this.ticketCreateRepresentationList = ticketCreateRepresentationList;
    }
}
