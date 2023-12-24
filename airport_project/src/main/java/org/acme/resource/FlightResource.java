package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;
import org.acme.persistence.FlightRepository;
import org.acme.representation.FlightMapper;
import org.acme.representation.FlightRepresentation;

import java.util.List;

import static org.acme.resource.AirportProjectURIs.FLIGHT;

@Path(FLIGHT)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class FlightResource {

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

}
