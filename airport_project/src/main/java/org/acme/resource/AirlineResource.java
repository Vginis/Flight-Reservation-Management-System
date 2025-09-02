package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.representation.AirlineCreateRepresentation;
import org.acme.representation.AirlineRepresentation;
import org.acme.representation.AirlineUpdateRepresentation;
import org.acme.service.AirlineService;

import java.util.List;

import static org.acme.resource.AirportProjectURIs.AIRLINES;

@Path(AIRLINES)
@RequestScoped
public class AirlineResource {

    @Inject
    AirlineService airlineService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AirlineRepresentation> searchByName(@QueryParam("name") String name) {
        return airlineService.searchAirlineByName(name);
    }

    @GET
    @Path("{id:[0-9]*}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByPathParamId(@PathParam("id") Integer id) {
        return Response.ok().entity(airlineService.searchAirlineById(id)).build();
    }

    @GET
    @Path("popularity/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMostPopularAirport(@PathParam("id") Integer id) {
        return Response.ok(airlineService.getMostPopularAirport(id),
                MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("completeness/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompletenessByAirlineId(@PathParam("id") Integer id) {
        return Response.ok(airlineService.getAirlineCompleteness(id),
                MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAirline(@Valid AirlineCreateRepresentation airlineDto) {
        airlineService.createAirline(airlineDto);
        //TODO Change response Message to created
        return Response.ok().build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAirline(AirlineUpdateRepresentation representation) {
        airlineService.updateAirlineDetails(representation);
        //TODO Add update success message
        return Response.noContent().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAirline(@PathParam("id") Integer id) {
        airlineService.deleteAirline(id);
        //TODO Add delete success message
        return Response.noContent().build();
    }
}
