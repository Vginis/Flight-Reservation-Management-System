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

import static org.acme.resource.AirportProjectURIs.FLIGHT;

@Path(FLIGHT)
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
    public List<FlightRepresentation> findByAirline(@QueryParam("airlineId") Integer airlineId) {
        return flightMapper.toRepresentationList(flightRepository.findByAirline(airlineId));
    }

    @PUT
    @Transactional
    public Response createFLight(FlightRepresentation flightDto) {
        if (flightDto.id == null) {
            throw new RuntimeException();
        }
        Flight flight = flightMapper.toModel(flightDto);
        flight = em.merge(flight);
        flightRepository.persist(flight);
        URI location = UriBuilder.fromResource(FlightResource.class).path(String.valueOf(flight.getId())).build();
        return Response
                .created(location)
                .entity(flightMapper.toRepresentation(flight))
                .build();
    }

    @PUT
    @Path("/{flightId}")
    @Transactional
    public Response updateFlightById(@PathParam("flightId") Integer id,
                                      FlightRepresentation representation) {
        if (!(id.equals(representation.id))) {
            throw new RuntimeException();
        }

        Flight flight = flightMapper.toModel(representation);
        flightRepository.getEntityManager().merge(flight);

        return Response.noContent().build();
    }

}
