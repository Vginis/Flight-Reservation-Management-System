package org.acme.representation;

import jakarta.inject.Inject;
import org.acme.domain.Passenger;
import org.acme.domain.Reservation;
import org.acme.persistence.ReservationRepository;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {ReservationMapper.class},
        imports = {Collectors.class})
public abstract class PassengerMapper {

    @Inject
    ReservationRepository reservationRepository;

    @Mapping(target = "reservationsId", expression = "java(passenger.getReservations().stream().map(org.acme.domain.Reservation::getReservationId).collect(Collectors.toList()))")
    public abstract PassengerRepresentation toRepresentation(Passenger passenger);

    public abstract List<PassengerRepresentation> toRepresentationList(List<Passenger> passengers);

    @Mapping(target = "reservations", ignore = true)
    public abstract Passenger toModel(PassengerRepresentation representation);

    @AfterMapping
    public void resolveReservationsById(PassengerRepresentation dto, @MappingTarget Passenger passenger) {
        List<Reservation> reservationsList = new ArrayList<>(dto.reservationsId.size());
        Reservation reservation = null;
        for (Integer i : dto.reservationsId) {
            if (i != null) {
                reservation = reservationRepository.find("reservationId", i).firstResultOptional().orElse(null);
            }
            reservationsList.add(reservation);
        }
        passenger.setReservations(reservationsList);
    }
}
