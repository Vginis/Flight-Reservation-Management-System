package org.acme.mapper;

import org.acme.domain.Luggage;
import org.acme.domain.Ticket;
import org.acme.representation.TicketRepresentation;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TicketMapper {

    private TicketMapper() {

    }

    public static TicketRepresentation mapEntityToRepresentation(Ticket ticket) {
        TicketRepresentation ticketRepresentation = new TicketRepresentation();
        ticketRepresentation.setTicketUUID(ticket.getTicketUUID());
        ticketRepresentation.setLuggageWeights(new ArrayList<>(
                ticket.getLuggages().stream().map(Luggage::getWeight).collect(Collectors.toSet())
        ));
        ticketRepresentation.setFirstName(ticket.getPassengerInfo().getFirstName());
        ticketRepresentation.setLastName(ticket.getPassengerInfo().getLastName());
        ticketRepresentation.setPassportId(ticket.getPassengerInfo().getPassport());
        return ticketRepresentation;
    }

}
