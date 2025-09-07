package org.acme.mapper;

import jakarta.inject.Inject;
import org.acme.domain.Passenger;
import org.acme.domain.Reservation;
import org.acme.persistence.ReservationRepository;
import org.acme.representation.PassengerRepresentation;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ReservationMapper.class},
        imports = {Collectors.class})
public abstract class PassengerMapper {
//
//    @Mapping(target = "reservationsId", expression = "java(passenger.getReservations().stream().map(org.acme.domain.Reservation::getReservationId).collect(Collectors.toList()))")
//    public abstract PassengerRepresentation toRepresentation(Passenger passenger);
//
//    public abstract List<PassengerRepresentation> toRepresentationList(List<Passenger> passengers);
//
//    @Mapping(target = "reservations", ignore = true)
//    public abstract Passenger toModel(PassengerRepresentation representation);
}
