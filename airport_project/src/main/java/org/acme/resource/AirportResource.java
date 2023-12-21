package org.acme.resource;

import java.net.URI;
import java.util.List;
import static org.acme.resource.AirportProjectURIs.AIRPORTS;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import org.acme.domain.Airport;
import org.acme.persistence.AirportRepository;
import org.acme.representation.AirportMapper;
import org.acme.representation.AirportRepresentation;

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
    @Path("{airportId:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("airportId") Integer airportId) {

        Airport airport = airportRepository.findById(airportId);
        if (airport == null) {
            return Response.status(Status.NOT_FOUND).build();
        }

        return Response.ok().entity(airportMapper.toRepresentation(airport)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<AirportRepresentation> searchAirportByName(@QueryParam("name") String name) {
        return airportMapper.toRepresentationList(airportRepository.search(name));
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create (AirportRepresentation representation) {
        if (representation.airportId == null) {
            throw new RuntimeException();
        }

        Airport airport= airportMapper.toModel(representation);
        airportRepository.persist(airport);
        URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(airport.getId())).build();
        return Response.created(uri).entity(airportMapper.toRepresentation(airport)).build();
    }

    @PUT
    @Path("/{airportId[0-9]*}")
    @Transactional
    public Response update(@PathParam("airportId") Integer id,
                           AirportRepresentation representation) {
        if (! id.equals(representation.airportId)) {
            throw new RuntimeException();
        }

        Airport borrower = airportMapper.toModel(representation);
        airportRepository.getEntityManager().merge(borrower);

        return Response.noContent().build();
    }
}