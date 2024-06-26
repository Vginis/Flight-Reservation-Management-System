package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.acme.domain.Airport;
import org.acme.domain.Flight;
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

    @Inject
    EntityManager em;

    @Context
    UriInfo uriInfo;

    @Inject
    AirportRepository airportRepository;

    @Inject
    AirportMapper airportMapper;

    @GET
    @Transactional
    public List<AirportRepresentation> findBy3DCode(@QueryParam("code") String code) {
        return airportMapper.toRepresentationList(airportRepository.findAirportBy3DCode(code));
    }

    @GET
    @Path("/{airportId}")
    @Transactional
    public Response findByPathParamId(@PathParam("airportId") Integer airportId) {
        Airport airport = airportRepository.findById(airportId);
        if (airport == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok().entity(airportMapper.toRepresentation(airport)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createAirport(AirportRepresentation airportDto) {
        Airport airport = airportMapper.toModel(airportDto);
        airport = em.merge(airport);
        airportRepository.persist(airport);
        URI location = uriInfo.getAbsolutePathBuilder().path(Integer.toString(airport.getAirportId())).build();
        return Response.created(location).entity(airportMapper.toRepresentation(airport)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateAirport(@PathParam("id") Integer id, AirportRepresentation representation) {
        if (!(id.equals(representation.airportId))) return Response.status(400).build();
        Airport airport = airportMapper.toModel(representation);
        airportRepository.getEntityManager().merge(airport);
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response removedAirport(@PathParam("id") Integer id) {
        Airport airport = airportRepository.find("id", id).firstResult();
        if (airport == null) return Response.status(404).build();
        airportRepository.deleteAirport(id);
        return Response.noContent().build();
    }
}