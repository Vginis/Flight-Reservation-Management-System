package org.acme.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.SuccessMessages;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.representation.aircraft.AircraftCreateRepresentation;
import org.acme.representation.aircraft.AircraftUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.service.AircraftService;
import org.acme.validation.EnumerationValue;

@Path(AirportProjectURIs.AIRCRAFTS)
public class AircraftResource {

    @Inject
    AircraftService aircraftService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByParams(@QueryParam("searchField") @EnumerationValue(acceptedEnum = AircraftSortAndFilterBy.class) String searchField,
                                   @QueryParam("searchValue") String searchValue,
                                   @QueryParam("size") @DefaultValue("10") Integer size,
                                   @QueryParam("index") @DefaultValue("0") Integer index,
                                   @QueryParam("sortBy") @EnumerationValue(acceptedEnum = AircraftSortAndFilterBy.class) String sortBy,
                                   @QueryParam("sortDirection") @EnumerationValue(acceptedEnum = SortDirection.class) String sortDirection){
        PageQuery<AircraftSortAndFilterBy> query = new PageQuery<>(ValueEnum.fromValue(searchField, AircraftSortAndFilterBy.class), searchValue, size, index
                , ValueEnum.fromValue(sortBy, AircraftSortAndFilterBy.class), ValueEnum.fromValue(sortDirection, SortDirection.class));
        return Response.ok(aircraftService.searchAircraftsByParams(query)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAircraft(@Valid AircraftCreateRepresentation aircraftCreateRepresentation){
        aircraftService.createAircraft(aircraftCreateRepresentation);
        return Response.ok(SuccessMessages.AIRCRAFT_CREATION_SUCCESS).build();
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAircraft(@Valid AircraftUpdateRepresentation aircraftUpdateRepresentation,
                                   @NotNull @PathParam("id") Integer id){
        aircraftService.updateAircraft(aircraftUpdateRepresentation, id);
        return Response.ok(SuccessMessages.AIRPORT_UPDATE_SUCCESS).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAircraft(@NotNull @PathParam("id") Integer id){
        aircraftService.deleteAircraft(id);
        return Response.ok(SuccessMessages.AIRCRAFT_DELETION_SUCCESS).build();
    }
}
