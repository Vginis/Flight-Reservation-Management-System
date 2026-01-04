package org.acme.representation.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.acme.representation.TicketRepresentation;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRepresentation {
    private String reservationUUID;
    private List<TicketRepresentation> ticketList;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime createdAt;

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
