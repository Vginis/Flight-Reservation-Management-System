package org.acme.representation;

import jakarta.inject.Inject;
import org.acme.domain.Flight;
import org.acme.domain.Passenger;
import org.acme.domain.Reservation;
import org.acme.persistence.FlightRepository;
import org.acme.persistence.PassengerRepository;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {TicketMapper.class},
        imports = {Collectors.class})
public abstract class ReservationMapper {

    @Inject
    PassengerRepository passengerRepository;

    @Inject
    FlightRepository flightRepository;

    @Mapping(target = "passengerId", source = "passenger.id")
    @Mapping(target = "outgoingFlights", expression = "java(reservation.getOutgoingFlights().stream().map(org.acme.domain.Flight::getFlightNo).collect(Collectors.toList()))")
    @Mapping(target = "ingoingFlights", expression = "java(reservation.getIngoingFlights().stream().map(org.acme.domain.Flight::getFlightNo).collect(Collectors.toList()))")
    @Mapping(target = "ticketList", source = "ticketsList")
    public abstract ReservationRepresentation toRepresentation(Reservation reservation);

    public abstract List<ReservationRepresentation> toRepresentationList(List<Reservation> reservations);

    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "outgoingFlights", ignore = true)
    @Mapping(target = "ingoingFlights", ignore = true)
    @Mapping(target = "ticketsList", source = "ticketList")
    @Mapping(target = "totalPrice", source = "totalPrice")
    public abstract Reservation toModel(ReservationRepresentation representation);


    @AfterMapping
    public void resolvePassengerById(ReservationRepresentation dto, @MappingTarget Reservation reservation) {
        Passenger passenger = null;
        if (dto.passengerId != null){
            passenger = passengerRepository.find("id", dto.passengerId)
                    .firstResultOptional().orElse(null);
        }
        reservation.setPassenger(passenger);
    }

    @AfterMapping
    public void resolveOutgoingFlightsByFlightNumber(ReservationRepresentation dto, @MappingTarget Reservation reservation) {
        List<Flight> outgoingFlights = new ArrayList<>(dto.outgoingFlights.size());
        Flight flight = null;
        for (String fr : dto.outgoingFlights) {
            if (fr != null) {
                flight = flightRepository.find("flightNo", fr).firstResultOptional().orElse(null);
            }
            outgoingFlights.add(flight);
        }
       reservation.setOutgoingFlights(outgoingFlights);
     }

    @AfterMapping
    public void resolveIngoingFlightsByFlightNumber(ReservationRepresentation dto, @MappingTarget Reservation reservation) {
        List<Flight> ingoingFlights = new ArrayList<>(dto.ingoingFlights.size());
        Flight flight = null;
        for (String fr : dto.ingoingFlights) {
            if (fr != null) {
                flight = flightRepository.find("flightNo", fr).firstResultOptional().orElse(null);
            }
            ingoingFlights.add(flight);
        }
        reservation.setIngoingFlights(ingoingFlights);
    }

}
