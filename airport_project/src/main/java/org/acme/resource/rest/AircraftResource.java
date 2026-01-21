package org.acme.resource.rest;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.representation.MessageRepresentation;
import org.acme.representation.aircraft.AircraftCreateUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.service.AircraftService;
import org.acme.validation.EnumerationValue;

@Path(AirportProjectURIs.AIRCRAFTS)
@Authenticated
public class AircraftResource {

    @Inject
    AircraftService aircraftService;

    @GET
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
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

    @GET
    @Path("smart-search")
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    public Response smartSearchAircraft(@QueryParam("aircraftName") String aircraftName){
        return Response.ok(aircraftService.smartSearchAircraft(aircraftName)).build();
    }

    @POST
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAircraft(@Valid AircraftCreateUpdateRepresentation aircraftCreateRepresentation){
        aircraftService.createAircraft(aircraftCreateRepresentation);
        return Response.ok(new MessageRepresentation(SuccessMessages.AIRCRAFT_CREATION_SUCCESS)).build();
    }

    @PUT
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAircraft(@Valid AircraftCreateUpdateRepresentation aircraftUpdateRepresentation,
                                   @NotNull @PathParam("id") Integer id){
        aircraftService.updateAircraft(aircraftUpdateRepresentation, id);
        return Response.ok(new MessageRepresentation(SuccessMessages.AIRCRAFT_UPDATE_SUCCESS)).build();
    }

    @DELETE
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAircraft(@NotNull @PathParam("id") Integer id){
        aircraftService.deleteAircraft(id);
        return Response.ok(new MessageRepresentation(SuccessMessages.AIRCRAFT_DELETION_SUCCESS)).build();
    }
}
