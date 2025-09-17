package org.acme.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.AirportSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.representation.airport.AirportCreateRepresentation;
import org.acme.representation.airport.AirportUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.service.AirportService;
import org.acme.validation.EnumerationValue;

import static org.acme.constant.AirportProjectURIs.AIRPORTS;

@Path(AIRPORTS)
@Authenticated
public class AirportResource {

    @Inject
    AirportService airportService;

    @GET
    @RolesAllowed(Role.SYSTEM_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByParams(@QueryParam("searchField") @EnumerationValue(acceptedEnum = AirportSortAndFilterBy.class) String searchField,
                                   @QueryParam("searchValue") String searchValue,
                                   @QueryParam("size") @DefaultValue("10") Integer size,
                                   @QueryParam("index") @DefaultValue("0") Integer index,
                                   @QueryParam("sortBy") @EnumerationValue(acceptedEnum = AirportSortAndFilterBy.class) String sortBy,
                                   @QueryParam("sortDirection") @EnumerationValue(acceptedEnum = SortDirection.class) String sortDirection){
        PageQuery<AirportSortAndFilterBy> query = new PageQuery<>(ValueEnum.fromValue(searchField, AirportSortAndFilterBy.class), searchValue, size, index
                , ValueEnum.fromValue(sortBy, AirportSortAndFilterBy.class), ValueEnum.fromValue(sortDirection, SortDirection.class));
        return Response.ok(airportService.searchAirportsByParams(query)).build();
    }

    @POST
    @RolesAllowed(Role.SYSTEM_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAirport(@Valid AirportCreateRepresentation airportDto) {
        airportService.createAirport(airportDto);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public Response updateAirport(@Valid AirportUpdateRepresentation representation) {
        airportService.updateAirport(representation);
        return Response.ok(SuccessMessages.AIRPORT_UPDATE_SUCCESS).build();
    }

    @DELETE
    @RolesAllowed(Role.SYSTEM_ADMIN)
    @Path("/{id}")
    public Response removeAirport(@PathParam("id") Integer id) {
        airportService.deleteAirport(id);
        return Response.ok(SuccessMessages.AIRPORT_DELETION_SUCCESS).build();
    }
}