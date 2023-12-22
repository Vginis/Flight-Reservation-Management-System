package org.acme.representation;

import org.acme.domain.Ticket;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TicketMapper {

    public abstract TicketRepresentation toRepresentation(Ticket ticket);

}
