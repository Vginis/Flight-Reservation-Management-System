package org.acme.util;

import org.acme.representation.reservation.ReservationCreateRepresentation;
import org.acme.representation.reservation.TicketCreateRepresentation;

import java.util.List;

public class ReservationUtil {

    public static ReservationCreateRepresentation createReservationCreateRepresentation(){
        ReservationCreateRepresentation reservationCreateRepresentation = new ReservationCreateRepresentation();
        reservationCreateRepresentation.setFlightUUID("1d2d5570-49a3-4f79-9a4f-653774c7cc64");
        reservationCreateRepresentation.setTicketCreateRepresentationList(List.of(createTicketCreateRepresentation()));
        return reservationCreateRepresentation;
    }

    public static TicketCreateRepresentation createTicketCreateRepresentation() {
        TicketCreateRepresentation ticketCreateRepresentation = new TicketCreateRepresentation();
        ticketCreateRepresentation.setFirstName("John");
        ticketCreateRepresentation.setLastName("Doe");
        ticketCreateRepresentation.setColumn(1);
        ticketCreateRepresentation.setRow(1);
        ticketCreateRepresentation.setLuggageWeights(List.of(12));
        ticketCreateRepresentation.setPassport("passport");
        return ticketCreateRepresentation;
    }
}
