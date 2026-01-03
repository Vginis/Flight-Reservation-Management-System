package org.acme.representation.reservation;

import org.acme.representation.TicketRepresentation;

import java.util.List;

public class ReservationRepresentation {
    private String reservationUUID;
    private List<TicketRepresentation> ticketList;

    public String getReservationUUID() {
        return reservationUUID;
    }

    public void setReservationUUID(String reservationUUID) {
        this.reservationUUID = reservationUUID;
    }

    public List<TicketRepresentation> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<TicketRepresentation> ticketList) {
        this.ticketList = ticketList;
    }
}
