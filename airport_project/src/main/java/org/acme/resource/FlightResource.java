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
import org.acme.domain.Flight;
import org.acme.persistence.FlightRepository;
import org.acme.mapper.FlightMapper;
import org.acme.representation.FlightRepresentation;

import java.net.URI;
import java.util.List;

import static org.acme.constant.AirportProjectURIs.FLIGHTS;

@Path(FLIGHTS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class FlightResource {

    @Inject
    EntityManager em;

    @Context
    UriInfo uriInfo;

    @Inject
    FlightRepository flightRepository;

    @Inject
    FlightMapper flightMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<FlightRepresentation> findByAirlineId(@QueryParam("airlineId") Integer airlineId) {
        return flightMapper.toRepresentationList(flightRepository.findFlightByAirlineId(airlineId));
    }
    //ToDo Implement search by params endpoint
//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Response createFLight(FlightRepresentation flightDto) {
//        Flight flight = flightMapper.toModel(flightDto);
//        flight = em.merge(flight);
//        flightRepository.persist(flight);
//        URI location = uriInfo.getAbsolutePathBuilder().path(Integer.toString(flight.getId())).build();
//        return Response.created(location).entity(flightMapper.toRepresentation(flight)).build();
//    }

//    @PUT
//    @Path("/{flightId}")
//    @Transactional
//    public Response updateFlight(@PathParam("flightId") Integer id, FlightRepresentation representation) {
//        if (!(id.equals(representation.id))) return Response.status(400).build();
//        Flight flight = flightMapper.toModel(representation);
//        flightRepository.getEntityManager().merge(flight);
//        return Response.noContent().build();
//    }

    @DELETE
    @Path("{id:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteFlight(@PathParam("id") Integer id) {
        Flight flight = flightRepository.find("id", id).firstResult();
        if (flight == null) return Response.status(404).build();
        flightRepository.deleteFlight(id);
        return Response.noContent().build();
    }

}
