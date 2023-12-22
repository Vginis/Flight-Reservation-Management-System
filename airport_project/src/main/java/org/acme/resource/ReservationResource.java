package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.domain.Reservation;
import org.acme.representation.ReservationMapper;
import org.acme.persistence.ReservationRepository;
import org.acme.representation.ReservationRepresentation;

import java.util.List;

import static org.acme.resource.AirportProjectURIs.RESERVATIONS;

@Path(RESERVATIONS)
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
    public List<ReservationRepresentation> searchReservationById(@QueryParam("reservationId") Integer reservationId) {
        return reservationMapper.toRepresentationList(reservationRepository.search(reservationId));
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

}
