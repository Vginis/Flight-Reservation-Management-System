package org.acme.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.AirlineSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.representation.MessageRepresentation;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.airline.AirlineUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.service.AirlineService;
import org.acme.validation.EnumerationValue;

import static org.acme.constant.AirportProjectURIs.AIRLINES;

@Path(AIRLINES)
@Authenticated
@RequestScoped
public class AirlineResource {

    @Inject
    AirlineService airlineService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchAirlinesByParams(@QueryParam("searchField") @EnumerationValue(acceptedEnum = AirlineSortAndFilterBy.class) String searchField,
                                           @QueryParam("searchValue") String searchValue,
                                           @QueryParam("size") @DefaultValue("10") Integer size,
                                           @QueryParam("index") @DefaultValue("0") Integer index,
                                           @QueryParam("sortBy") @EnumerationValue(acceptedEnum = AirlineSortAndFilterBy.class) String sortBy,
                                           @QueryParam("sortDirection") @EnumerationValue(acceptedEnum = SortDirection.class) String sortDirection){
        PageQuery<AirlineSortAndFilterBy> query = new PageQuery<>(ValueEnum.fromValue(searchField, AirlineSortAndFilterBy.class), searchValue, size, index
                , ValueEnum.fromValue(sortBy, AirlineSortAndFilterBy.class), ValueEnum.fromValue(sortDirection, SortDirection.class));
        return Response.ok(airlineService.searchAirlinesByParams(query)).build();
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
    @RolesAllowed(Role.SYSTEM_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAirline(@Valid AirlineCreateRepresentation airlineDto) {
        airlineService.createAirline(airlineDto);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @RolesAllowed(Role.SYSTEM_ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAirline(@Valid AirlineUpdateRepresentation representation) {
        airlineService.updateAirlineDetails(representation);
        return Response.ok(new MessageRepresentation(SuccessMessages.AIRLINE_UPDATE_SUCCESS)).build();
    }

    @DELETE
    @RolesAllowed(Role.SYSTEM_ADMIN)
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteAirline(@PathParam("id") Integer id) {
        airlineService.deleteAirline(id);
        return Response.ok(new MessageRepresentation(SuccessMessages.AIRLINE_DELETION_SUCCESS)).build();
    }
}
