package org.acme.representation.reservation;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.acme.representation.TicketRepresentation;

import java.util.List;

@RegisterForReflection
public class ReservationRepresentation {

    public Integer reservationId;
    public Integer passengerId;
    public List<String> outgoingFlights;
    public List<String> ingoingFlights;
    public List<TicketRepresentation> ticketList;
    public Long totalPrice;

}
