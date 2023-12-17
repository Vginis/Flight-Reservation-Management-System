package org.acme.resource;

import java.util.List;
import static org.acme.resource.ConvergenceUri.AIRPORTS;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriInfo;

import org.acme.domain.Airport;
import org.acme.persistence.AirportRepository;
import org.acme.representation.AirportMapper;
import org.acme.representation.AirportRepresentation;

@Path(AIRPORTS)
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
    public List<AirportRepresentation> searchBookByTitle(@QueryParam("name") String name) {
        return airportMapper.toRepresentationList(airportRepository.search(name));
    }

}