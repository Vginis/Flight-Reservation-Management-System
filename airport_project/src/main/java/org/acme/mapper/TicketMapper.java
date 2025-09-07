package org.acme.mapper;

import org.acme.domain.Flight;
import jakarta.inject.Inject;
import org.acme.domain.Reservation;
import org.acme.domain.Ticket;
import org.acme.persistence.FlightRepository;
import org.acme.persistence.ReservationRepository;
import org.acme.representation.TicketRepresentation;
import org.mapstruct.*;

@Mapper(componentModel = "jakarta",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TicketMapper {

//    @Inject
//    FlightRepository flightRepository;
//    @Inject
//    ReservationRepository reservationRepository;
//
//
//    @Mapping(target = "reservationId", source = "reservation.reservationId")
//    @Mapping(target = "flightNo", source = "flight.flightNo")
//    public abstract TicketRepresentation toRepresentation(Ticket ticket);
//
//    @Mapping(target = "flight", ignore = true)
//    @Mapping(target = "reservation", ignore = true)
//    public abstract Ticket toModel(TicketRepresentation representation);
//
//    @AfterMapping
//    public void resolveFlightByNo(TicketRepresentation dto, @MappingTarget Ticket ticket) {
//        Flight flight = null;
//        if (dto.flightNo != null) {
//            flight = flightRepository.find("flightNo", dto.flightNo)
//                    .firstResultOptional().orElse(null);
//        }
//        ticket.setFlight(flight);
//    }
//
//    @AfterMapping
//    public void resolveReservationById(TicketRepresentation dto, @MappingTarget Ticket ticket) {
//        Reservation reservation = null;
//        if (dto.reservationId != null){
//            reservation = reservationRepository.find("reservationId", dto.reservationId)
//                    .firstResultOptional().orElse(null);
//        }
//        ticket.setReservation(reservation);
//    }

}
