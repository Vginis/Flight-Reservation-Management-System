package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.acme.domain.Airline;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.AirlineMapper;
import org.acme.representation.AirlineRepresentation;


import java.net.URI;
import java.util.List;

import static org.acme.resource.AirportProjectURIs.AIRLINES;

@Path(AIRLINES)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class AirlineResource {

    @Context
    UriInfo uriInfo;

    @Inject
    AirlineRepository airlineRepository;

    @Inject
    AirlineMapper airlineMapper;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response find(@PathParam("id") Integer id) {

        Airline airline = airlineRepository.findById(id);
        if (airline == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok().entity(airlineMapper.toRepresentation(airline)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<AirlineRepresentation> searchAirportByName(@QueryParam("name") String name) {
        return airlineMapper.toRepresentationList(airlineRepository.search(name));
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response submitAirline(AirlineRepresentation airlineDto){
        Airline airline = airlineMapper.toModel(airlineDto);
        airlineRepository.persist(airline);
        URI location = uriInfo.getAbsolutePathBuilder().path(
                Integer.toString(airline.getId())).build();
        return Response
                .created(location)
                .entity(airlineMapper.toRepresentation(airline))
                .build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response updateAirline(@PathParam("id") Integer id,
                                      AirlineRepresentation representation) {
        if (!(id.equals(representation.id))) {
            throw new RuntimeException();
        }

        Airline airline = airlineMapper.toModel(representation);
        airlineRepository.getEntityManager().merge(airline);

        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response deleteAirline(@PathParam("id") Integer id){
        Airline airline = airlineRepository.find("id",id).firstResult();
        if (airline==null){
            return Response.status(404).build();
        }
        airlineRepository.deleteAirline(id);
        return Response.noContent().build();
    }
}
