package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.SuccessMessages;
import org.acme.representation.AirlineCreateRepresentation;
import org.acme.representation.AirlineRepresentation;
import org.acme.representation.AirlineUpdateRepresentation;
import org.acme.service.AirlineService;

import static org.acme.constant.AirportProjectURIs.AIRLINES;

@Path(AIRLINES)
@RequestScoped
public class AirlineResource {

    @Inject
    AirlineService airlineService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public AirlineRepresentation searchByName(@QueryParam("name") String name) {
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
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAirline(@Valid AirlineUpdateRepresentation representation) {
        airlineService.updateAirlineDetails(representation);
        return Response.ok(SuccessMessages.AIRLINE_UPDATE_SUCCESS).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAirline(@PathParam("id") Integer id) {
        airlineService.deleteAirline(id);
        return Response.noContent().build();
    }
}
