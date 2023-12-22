package org.acme.representation;

import org.acme.domain.Reservation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TicketMapper.class})
public abstract class ReservationMapper {

    public abstract ReservationRepresentation toRepresentation(Reservation reservation);

    public abstract List<ReservationRepresentation> toRepresentationList(List<Reservation> reservations);

    public abstract Reservation toModel(ReservationRepresentation representation);

}
