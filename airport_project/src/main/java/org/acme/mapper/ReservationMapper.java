package org.acme.mapper;

import jakarta.inject.Inject;
import org.acme.domain.Flight;
import org.acme.domain.Passenger;
import org.acme.domain.Reservation;
import org.acme.persistence.FlightRepository;
import org.acme.persistence.PassengerRepository;
import org.acme.representation.ReservationRepresentation;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TicketMapper.class},
        imports = {Collectors.class})
public abstract class ReservationMapper {

//    @Mapping(target = "passengerId", source = "passenger.id")
//    @Mapping(target = "outgoingFlights", expression = "java(reservation.getOutgoingFlights().stream().map(org.acme.domain.Flight::getFlightNo).collect(Collectors.toList()))")
//    @Mapping(target = "ingoingFlights", expression = "java(reservation.getIngoingFlights().stream().map(org.acme.domain.Flight::getFlightNo).collect(Collectors.toList()))")
//    @Mapping(target = "ticketList", source = "ticketsList")
//    public abstract ReservationRepresentation toRepresentation(Reservation reservation);
//
//    public abstract List<ReservationRepresentation> toRepresentationList(List<Reservation> reservations);
//
//    @Mapping(target = "passenger", ignore = true)
//    @Mapping(target = "outgoingFlights", ignore = true)
//    @Mapping(target = "ingoingFlights", ignore = true)
//    @Mapping(target = "ticketsList", source = "ticketList")
//    @Mapping(target = "totalPrice", source = "totalPrice")
//    public abstract Reservation toModel(ReservationRepresentation representation);

}
