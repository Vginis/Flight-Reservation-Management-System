package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.domain.Passenger;
import org.acme.persistence.PassengerRepository;
import org.acme.representation.PassengerMapper;
import org.acme.representation.PassengerRepresentation;

import java.net.URI;
import java.util.List;

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
    EntityManager em;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("id") Integer id) {

        Passenger passenger = passengerRepository.findById(id);
        if (passenger == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(passengerMapper.toRepresentation(passenger)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<PassengerRepresentation> searchPassengerByEmail(@QueryParam("email") String email) {
        return passengerMapper.toRepresentationList(passengerRepository.findByEmail(email));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createPassenger(PassengerRepresentation passengerDto){
        Passenger passenger = passengerMapper.toModel(passengerDto);
        passenger = em.merge(passenger);
        passengerRepository.persist(passenger);
        URI location = uriInfo.getAbsolutePathBuilder().path(
                Integer.toString(passenger.getId())).build();
        return Response
                .created(location)
                .entity(passengerMapper.toRepresentation(passenger))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updatePassengerById(@PathParam("id") Integer passengerid,
                                        PassengerRepresentation representation) {
        if (!(passengerid.equals(representation.id))) {
            throw new RuntimeException();
        }

        Passenger passenger = passengerMapper.toModel(representation);
        passengerRepository.getEntityManager().merge(passenger);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deletePassenger(@PathParam("id") Integer id){
        Passenger passenger = passengerRepository.find("id",id).firstResult();
        if (passenger==null){
            return Response.status(404).build();
        }
        passengerRepository.deletePassenger(id);
        return Response.noContent().build();
    }


}
