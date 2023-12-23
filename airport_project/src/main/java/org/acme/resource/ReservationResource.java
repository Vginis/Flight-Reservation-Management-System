package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.acme.domain.Reservation;
import org.acme.representation.ReservationMapper;
import org.acme.persistence.ReservationRepository;
import org.acme.representation.ReservationRepresentation;

import java.net.URI;
import java.util.List;

import static org.acme.resource.AirportProjectURIs.RESERVATIONS;

@Path(RESERVATIONS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class ReservationResource {

    @Context
    UriInfo uriInfo;

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    ReservationMapper reservationMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<ReservationRepresentation> searchReservationByPassengerId(@QueryParam("passengerId") Integer passengerId) {
        return reservationMapper.toRepresentationList(reservationRepository.search(passengerId));
    }

    @GET
    @Path("{reservationId:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("reservationId") Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId);
        if (reservation == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(reservationMapper.toRepresentation(reservation)).build();
    }

    /*@PUT
    @Transactional
    public Response createReservation(ReservationRepresentation representation) {
        if (representation.reservationId == null) {
            throw new RuntimeException();
        }
        Reservation reservation = reservationMapper.toModel(representation);
        reservationRepository.persist(reservation);
        URI uri = UriBuilder.fromResource(ReservationResource.class).path(String.valueOf(reservation.getReservationId())).build();
        return Response.created(uri).entity(reservationMapper.toRepresentation(reservation)).build();
    }*/

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createReservation(ReservationRepresentation reservationDto){
        Reservation reservation = reservationMapper.toModel(reservationDto);
        reservationRepository.persist(reservation);
        URI location = uriInfo.getAbsolutePathBuilder().path(
                Integer.toString(reservation.getReservationId())).build();
        return Response
                .created(location)
                .entity(reservationMapper.toRepresentation(reservation))
                .build();
    }

}
