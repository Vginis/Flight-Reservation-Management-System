package org.acme.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.SuccessMessages;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.service.FlightService;
import org.acme.validation.EnumerationValue;

import static org.acme.constant.AirportProjectURIs.FLIGHTS;

@Path(FLIGHTS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RequestScoped
public class FlightResource {

    @Inject
    FlightService flightService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByParams(@QueryParam("searchField") @EnumerationValue(acceptedEnum = FlightSortAndFilterBy.class) String searchField,
                                   @QueryParam("searchValue") String searchValue,
                                   @QueryParam("size") @DefaultValue("10") Integer size,
                                   @QueryParam("index") @DefaultValue("0") Integer index,
                                   @QueryParam("sortBy") @EnumerationValue(acceptedEnum = FlightSortAndFilterBy.class) String sortBy,
                                   @QueryParam("sortDirection") @EnumerationValue(acceptedEnum = SortDirection.class) String sortDirection){
        PageQuery<FlightSortAndFilterBy> query = new PageQuery<>(ValueEnum.fromValue(searchField, FlightSortAndFilterBy.class), searchValue, size, index
                , ValueEnum.fromValue(sortBy, FlightSortAndFilterBy.class), ValueEnum.fromValue(sortDirection, SortDirection.class));
        return Response.ok(flightService.searchFlightsByParams(query)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFLight(@Valid FlightCreateRepresentation flightCreateRepresentation) {
        flightService.createFlight(flightCreateRepresentation);
        return Response.ok(SuccessMessages.FLIGHT_CREATION_SUCCESS).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{flightId}")
    public Response updateFlight(@PathParam("flightId") Integer id, @Valid FlightDateUpdateRepresentation representation) {
        flightService.updateFlightDates(representation, id);
        return Response.ok(SuccessMessages.FLIGHT_UPDATE_SUCCESS).build();
    }

    @DELETE
    @Path("{id:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteFlight(@PathParam("id") Integer id) {
        flightService.deleteFlight(id);
        return Response.ok(SuccessMessages.FLIGHT_DELETE_SUCCESS).build();
    }

}
