package org.acme.representation;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class ReservationRepresentation {
    public Integer reservationId;
    public Integer passengerId;
    //public List<FlightRepresentation> outgoingFlights;
    //public List<FlightRepresentation> ingoingFlights;
    public List<TicketRepresentation> ticketsList;
    public Long totalPrice;

}
