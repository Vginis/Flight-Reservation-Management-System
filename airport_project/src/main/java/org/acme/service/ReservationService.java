package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.SeatReservationState;
import org.acme.domain.Flight;
import org.acme.domain.FlightInformation;
import org.acme.domain.FlightSeat;
import org.acme.domain.Luggage;
import org.acme.domain.PassengerInfo;
import org.acme.domain.Reservation;
import org.acme.domain.Ticket;
import org.acme.domain.User;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.persistence.FlightRepository;
import org.acme.persistence.ReservationRepository;
import org.acme.persistence.UserRepository;
import org.acme.representation.reservation.ReservationCreateRepresentation;
import org.acme.representation.reservation.TicketCreateRepresentation;
import org.acme.util.UserContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class ReservationService {

    @Inject
    ReservationRepository reservationRepository;
    @Inject
    FlightRepository flightRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    UserContext userContext;

    @Transactional
    public void createReservation(ReservationCreateRepresentation reservationCreateRepresentation){
        String loggedInUsername = userContext.extractUsername();
        Optional<User> userOptional = userRepository.findUserByUsername(loggedInUsername);
        Optional<Flight> flightOptional = flightRepository.getFlightByUUID(reservationCreateRepresentation.getFlightUUID());

        if(userOptional.isEmpty() || flightOptional.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Flight flight = flightOptional.get();
        List<Ticket> tickets = loadTickets(flight, reservationCreateRepresentation.getTicketCreateRepresentationList());
        User user = userOptional.get();
        Reservation reservation = new Reservation(user, tickets);
        reservation.getTicketsList().forEach(ticket -> ticket.setReservation(reservation));
        reservationRepository.persist(reservation);
    }

    private List<Ticket> loadTickets(Flight flight, List<TicketCreateRepresentation> ticketCreateRepresentationList) {
        List<Ticket> tickets = new ArrayList<>();
        for(TicketCreateRepresentation ticketRepresentation: ticketCreateRepresentationList) {
            PassengerInfo passengerInfo = new PassengerInfo(ticketRepresentation);
            FlightInformation flightInformation = new FlightInformation(flight.getFlightNumber(),flight.getFlightUUID());

            List<Luggage> luggages = loadLuggages(ticketRepresentation.getLuggageWeights());
            Ticket ticket = new Ticket(luggages, flightInformation, passengerInfo);

            ticket.getLuggages().forEach(luggage -> luggage.setTicket(ticket));
            tickets.add(ticket);

            this.updateSeat(flight, ticketRepresentation);
        }

        return tickets;
    }

    private List<Luggage> loadLuggages(List<Integer> luggageWeights) {
        List<Luggage> luggages = new ArrayList<>();
        for(Integer weight: luggageWeights) {
            Luggage luggage = new Luggage(weight);
            luggages.add(luggage);
        }
        return luggages;
    }

    private void updateSeat(Flight flight, TicketCreateRepresentation ticketRepresentation) {
        Optional<FlightSeat> flightSeatStateOptional = flight.getFlightSeatLayout().getFlightSeats()
            .stream().filter(seat -> Objects.equals(seat.getSeatRow(), ticketRepresentation.getRow())
                    && seat.getSeatColumn().equals(ticketRepresentation.getColumn())).findFirst();

        if(flightSeatStateOptional.isEmpty()) {
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        FlightSeat flightSeatState = flightSeatStateOptional.get();
        String username = userContext.extractUsername();
        if(!flightSeatState.getLastUpdatedBy().equals(username) || !flightSeatState.getState().equals(SeatReservationState.LOCKED)) {
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }
        flightSeatState.updateSeatState(SeatReservationState.BOOKED, username);
    }

}
