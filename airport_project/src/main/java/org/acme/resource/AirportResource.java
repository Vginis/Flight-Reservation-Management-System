package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.SuccessMessages;
import org.acme.representation.AirportCreateRepresentation;
import org.acme.service.AirportService;

import static org.acme.constant.AirportProjectURIs.AIRPORTS;

@Path(AIRPORTS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AirportResource {

    @Inject
    AirportService airportService;

    @GET
    public Response findBy3DCode(@QueryParam("code") String code) {
        return Response.ok().entity(airportService.findAirportBy3DCode(code)).build();
    }

    @GET
    @Path("/{airportId}")
    public Response findByPathParamId(@PathParam("airportId") Integer airportId) {
        return Response.ok().entity(airportService.findAirportById(airportId)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAirport(@Valid AirportCreateRepresentation airportDto) {
        airportService.createAirport(airportDto);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    public Response updateAirport(@Valid AirportCreateRepresentation representation) {
        airportService.updateAirport(representation);
        return Response.ok(SuccessMessages.AIRPORT_UPDATE_SUCCESS).build();
    }

    @DELETE
    @Path("/{id}")
    public Response removedAirport(@PathParam("id") Integer id) {
        airportService.deleteAirport(id);
        return Response.ok(SuccessMessages.AIRPORT_DELETION_SUCCESS).build();
    }
}