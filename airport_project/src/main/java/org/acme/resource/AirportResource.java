package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.core.UriBuilder;
import org.acme.domain.Airport;
import org.acme.persistence.AirportRepository;
import org.acme.representation.AirportMapper;
import org.acme.representation.AirportRepresentation;

import java.net.URI;
import java.util.List;

import static org.acme.resource.AirportProjectURIs.AIRPORTS;

@Path(AIRPORTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequestScoped
public class AirportResource {

    @Context
    UriInfo uriInfo;

    @Inject
    AirportRepository airportRepository;

    @Inject
    AirportMapper airportMapper;

    @GET
    @Transactional
    public List<AirportRepresentation> listAll() {
        return airportMapper.toRepresentationList(airportRepository.listAll());
    }

    @GET
    @Path("/{airportId}")
    @Transactional
    public Response findAirportById(@PathParam("airportId") Integer airportId) {

        Airport airport = airportRepository.findAirportById(airportId);
        if (airport == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(airportMapper.toRepresentation(airport)).build();
    }

    @PUT
    @Transactional
    public Response createAirport(AirportRepresentation airportDto) {
        if (airportDto.airportId == null) {
            throw new RuntimeException();
        }
        Airport airport = airportMapper.toModel(airportDto);
        airportRepository.persist(airport);
        //URI location = uriInfo.getAbsolutePathBuilder().path((airport.getName())).build();
        URI location = UriBuilder.fromResource(AirportResource.class).path(String.valueOf(airport.getAirportId())).build();
        return Response
                .created(location)
                .entity(airportMapper.toRepresentation(airport))
                .build();
    }
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateAirportById(@PathParam("id") Integer id,
                           AirportRepresentation representation) {
        if (!(id.equals(representation.airportId))) {
            throw new RuntimeException();
        }

        Airport airport = airportMapper.toModel(representation);
        airportRepository.getEntityManager().merge(airport);

        return Response.noContent().build();
    }

}