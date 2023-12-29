package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.domain.*;
import org.acme.persistence.*;
import org.acme.representation.FlightMapper;
import org.acme.representation.PassengerMapper;
import org.acme.representation.PassengerRepresentation;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static org.acme.resource.AirportProjectURIs.PASSENGERS;

@Path(PASSENGERS)
@RequestScoped
public class PassengerResource {

    @Context
    UriInfo uriInfo;

    @Inject
    PassengerRepository passengerRepository;

    @Inject
    PassengerMapper passengerMapper;

    @Inject
    FlightRepository flightRepository;

    @Inject
    FlightMapper flightMapper;

    @Inject
    AirlineRepository airlineRepository;

    @Inject
    EntityManager em;
    @Inject
    ReservationRepository reservationRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PassengerRepresentation> findByEmail(@QueryParam("email") String email) {
        return passengerMapper.toRepresentationList(passengerRepository.findPassengerByEmail(email));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response findByPathParamId(@PathParam("id") Integer id) {
        Passenger passenger = passengerRepository.findById(id);
        if (passenger == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok().entity(passengerMapper.toRepresentation(passenger)).build();
    }

    @GET
    @Path("/searchForFlights/{airlineId}/{destAirport}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response findFlightsByDestination(@PathParam("airlineId") Integer airlineId, @PathParam("destAirport") String arrAirportName) {
        Airline airline = airlineRepository.findById(airlineId);
        if(airline == null) return null;
        List<Flight> flightList = new ArrayList<>();
        for (Flight f :airline.getFlights()){
            if (Objects.equals(f.getArrivalAirport().getAirportName(), arrAirportName))
                flightList.add(f);
        }
        if (flightList.isEmpty()) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok().entity(flightMapper.toRepresentationList(flightList)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createPassenger(PassengerRepresentation passengerDto) {
        Passenger passenger = passengerMapper.toModel(passengerDto);
        passenger = em.merge(passenger);
        passengerRepository.persist(passenger);
        URI location = uriInfo.getAbsolutePathBuilder().path(Integer.toString(passenger.getId())).build();
        return Response.created(location).entity(passengerMapper.toRepresentation(passenger)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePassenger(@PathParam("id") Integer passengerId, PassengerRepresentation representation) {
        if (!(passengerId.equals(representation.id))) return Response.status(400).build();
        Passenger passenger = passengerMapper.toModel(representation);
        passengerRepository.getEntityManager().merge(passenger);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deletePassenger(@PathParam("id") Integer id) {
        Passenger passenger = passengerRepository.find("id",id).firstResult();
        if (passenger == null) return Response.status(404).build();
        passengerRepository.deletePassenger(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/{id}/makeReservation/{lastName}/{firstName}/{passport}/{flightNo}/{lugWeight}/{lugAmount}/{seatNo}")
    @Transactional
    public Response makeReservation(@PathParam("id") Integer id,
                                    @PathParam("flightNo") String flightNo,
                                    @PathParam("lugWeight") Integer lugWeight,
                                    @PathParam("lugAmount") Integer lugAmount,
                                    @PathParam("firstName") String firstName,
                                    @PathParam("lastName") String lastName,
                                    @PathParam("seatNo") String seatNo,
                                    @PathParam("passport") String passport) {
        Reservation reservation = createReservation(id, flightNo, lugAmount, lugWeight, firstName, lastName, seatNo, passport);
        if (reservation == null) return Response.status(404).build();
        if (reservation.getOutgoingFlights().isEmpty()) return Response.status(406).build();
        reservation = em.merge(reservation);
        reservationRepository.persist(reservation);
        URI makingReservationURI = uriInfo.getBaseUriBuilder().path("Reservations").path(Integer.toString(reservation.getReservationId())).build();
        return Response.created(makingReservationURI).build();
    }

    @DELETE
    @Path("/{id}/deleteReservation/{reservationId}")
    @Transactional
    public Response deleteReservation(@PathParam("id") Integer id, @PathParam("reservationId") Integer reservationId) {
        List<Reservation> reservations = reservationRepository.findReservationByPassengerId(id);
        for (Reservation r : reservations) {
            if (r.getReservationId().equals(reservationId)) {
                reservationRepository.delete(r);
                return Response.noContent().build();
            }
        }
        return Response.status(404).build();
    }

    @Transactional
    protected Reservation createReservation(Integer id, String flightNo, Integer lugAmount, Integer lugWeight, String firstName, String lastName, String seatNo, String passport) {
        Passenger passenger = passengerRepository.findById(id);
        if (passenger == null) return null;
        Flight flight = flightRepository.find("flightNo",flightNo).firstResult();
        if (flight == null) return null;
        Reservation reservation = new Reservation();
        Ticket ticket = new Ticket();
        ticket.setReservation(reservation);
        ticket.setFlight(flight);
        ticket.setTicketPrice(flight.getTicketPrice());
        ticket.setAmount(lugAmount);
        ticket.setWeight(lugWeight);
        ticket.setLuggageIncluded(true);
        ticket.setFirstName(firstName);
        ticket.setLastName(lastName);
        ticket.setSeatNo(seatNo);
        ticket.setPassportId(passport);
        reservation.addTicket(ticket);
        reservation.setPassenger(passenger);
        reservation.addOutgoingFlight(flight);
        return reservation;
    }

}
