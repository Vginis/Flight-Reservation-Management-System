package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.acme.domain.Flight;
import org.acme.persistence.FlightRepository;
import org.acme.representation.FlightMapper;
import org.acme.representation.FlightRepresentation;

import java.net.URI;
import java.util.List;

import static org.acme.resource.AirportProjectURIs.FLIGHTS;

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
    //TODO testaki kai na ftiaksoume mia get:searchByDepartureAirport,ArrivalAirport,Departure/ArrivalDate,PassengerCount me QueryParams
    @GET
    @Path("{flightId:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response findByPathParamId(@PathParam("flightId") Integer flightId) {
        Flight flight = flightRepository.findById(flightId);
        if (flight == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok().entity(flightMapper.toRepresentation(flight)).build();
    }


    //TODO DELETE
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createFLight(FlightRepresentation flightDto) {
        Flight flight = flightMapper.toModel(flightDto);
        flight = em.merge(flight);
        flightRepository.persist(flight);
        URI location = uriInfo.getAbsolutePathBuilder().path(Integer.toString(flight.getId())).build();
        return Response.created(location).entity(flightMapper.toRepresentation(flight)).build();
    }

    @PUT
    @Path("/{flightId}")
    @Transactional
    public Response updateFlight(@PathParam("flightId") Integer id, FlightRepresentation representation) {
        if (!(id.equals(representation.id))) return Response.status(400).build();
        Flight flight = flightMapper.toModel(representation);
        flightRepository.getEntityManager().merge(flight);
        return Response.noContent().build();
    }

}
