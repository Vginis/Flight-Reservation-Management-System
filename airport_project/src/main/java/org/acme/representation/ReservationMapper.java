package org.acme.representation;

import jakarta.inject.Inject;
import org.acme.domain.Reservation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TicketMapper.class},
        imports = {Collectors.class})
public abstract class ReservationMapper {

    @Inject
    FlightMapper flightMapper;

    @Mapping(target = "passengerId", source = "passenger.id")
    @Mapping(target = "outgoingFlights", expression = "java(reservation.getOutgoingFlights().stream().map(org.acme.domain.Flight::getFlightNo).collect(Collectors.toList()))")
    @Mapping(target = "ingoingFlights", expression = "java(reservation.getIngoingFlights().stream().map(org.acme.domain.Flight::getFlightNo).collect(Collectors.toList()))")
    public abstract ReservationRepresentation toRepresentation(Reservation reservation);

    public abstract List<ReservationRepresentation> toRepresentationList(List<Reservation> reservations);

    @Mapping(target = "passenger.id", source = "passengerId")
    @Mapping(target = "outgoingFlights", ignore = true)
    @Mapping(target = "ingoingFlights", ignore = true)
    @Mapping(target = "ticketsList", ignore = true)
    public abstract Reservation toModel(ReservationRepresentation representation);

}
