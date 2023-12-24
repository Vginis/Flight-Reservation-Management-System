package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
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
    /* TODO Δεν χρειάζεται...το κάνει η passengerRepository.searchByEmail()
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public  List<PassengerRepresentation> list() {
        return passengerMapper.toRepresentationList(passengerRepository.listAll());
    }*/

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createPassenger(PassengerRepresentation passengerDto){
        Passenger passenger = passengerMapper.toModel(passengerDto);
        passengerRepository.persist(passenger);
        URI location = uriInfo.getAbsolutePathBuilder().path(
                Integer.toString(passenger.getId())).build();
        return Response
                .created(location)
                .entity(passengerMapper.toRepresentation(passenger))
                .build();
    }
}
