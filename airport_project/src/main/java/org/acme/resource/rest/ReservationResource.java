package org.acme.resource.rest;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import static org.acme.constant.AirportProjectURIs.RESERVATIONS;

@Path(RESERVATIONS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {


//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public List<ReservationRepresentation> findByPassengerId(@QueryParam("passengerId") Integer passengerId) {
//        return reservationMapper.toRepresentationList(reservationRepository.findReservationByPassengerId(passengerId));
//    }

//    @GET
//    @Path("{reservationId:[0-9]*}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Response findByPathParamId(@PathParam("reservationId") Integer reservationId) {
//        Reservation reservation = reservationRepository.findById(reservationId);
//        if (reservation == null) return Response.status(Response.Status.NOT_FOUND).build();
//        return Response.ok().entity(reservationMapper.toRepresentation(reservation)).build();
//    }
//
//    @POST
//    @Produces(MediaType.APPLICATION_JSON)
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Response createReservation(ReservationRepresentation reservationDto) {
//        Reservation reservation = reservationMapper.toModel(reservationDto);
//        reservationRepository.persist(reservation);
//        URI location = uriInfo.getAbsolutePathBuilder().path(Integer.toString(reservation.getReservationId())).build();
//        return Response.created(location).entity(reservationMapper.toRepresentation(reservation)).build();
//    }

//    @PUT
//    @Path("/{reservationId}")
//    @Transactional
//    public Response updateReservation(@PathParam("reservationId") Integer id, ReservationRepresentation representation) {
//        if (!(id.equals(representation.reservationId))) return Response.status(400).build();
//        Reservation reservation = reservationMapper.toModel(representation);
//        reservationRepository.getEntityManager().merge(reservation);
//        return Response.noContent().build();
//    }

    @DELETE
    @Path("/{reservationId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteReservation(@PathParam("reservationId") Integer id) {
        return Response.noContent().build();
    }

}
