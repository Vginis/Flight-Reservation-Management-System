package org.acme.representation;

import org.acme.domain.Ticket;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TicketMapper {

    //@Mapping(target = "reservation", source = "reservation.reservationId")
    @Mapping(target = "flightNo", source = "flight.flightNo")
    public abstract TicketRepresentation toRepresentation(Ticket ticket);

    @Mapping(target = "flight.flightNo", source = "flightNo")
    public abstract Ticket toModel(TicketRepresentation representation);

}
