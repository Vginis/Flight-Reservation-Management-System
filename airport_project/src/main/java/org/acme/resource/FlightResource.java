package org.acme.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.FlightStatus;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.FlightSearchFilterParamsDTO;
import org.acme.constant.search.FlightSearchParamsDTO;
import org.acme.constant.search.FlightSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.representation.MessageRepresentation;
import org.acme.representation.flight.FlightCreateRepresentation;
import org.acme.representation.flight.FlightDateUpdateRepresentation;
import org.acme.representation.flight.FlightMultipleParamsSearchDTO;
import org.acme.search.FlightPageQuery;
import org.acme.service.FlightService;
import org.acme.validation.EnumerationValue;

import static org.acme.constant.AirportProjectURIs.FLIGHTS;

@Path(FLIGHTS)
@Authenticated
@RequestScoped
public class FlightResource {

    @Inject
    FlightService flightService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByParams(@QueryParam("searchField") @EnumerationValue(acceptedEnum = FlightSortAndFilterBy.class) String searchField,
                                   @QueryParam("searchValue") String searchValue,
                                   @QueryParam("departureDate") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$") String departureDate,
                                   @QueryParam("arrivalDate") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$") String arrivalDate,
                                   @QueryParam("departureAirport") Integer departureAirportId,
                                   @QueryParam("arrivalAirport") Integer arrivalAirportId,
                                   @QueryParam("size") @DefaultValue("10") Integer size,
                                   @QueryParam("index") @DefaultValue("0") Integer index,
                                   @QueryParam("sortBy") @EnumerationValue(acceptedEnum = FlightSortAndFilterBy.class) String sortBy,
                                   @QueryParam("sortDirection") @EnumerationValue(acceptedEnum = SortDirection.class) String sortDirection){
        FlightSearchFilterParamsDTO flightSearchFilterParamsDTO = new FlightSearchFilterParamsDTO(searchField, searchValue,
                departureDate, arrivalDate, departureAirportId, arrivalAirportId);
        FlightSearchParamsDTO flightSearchParamsDTO = new FlightSearchParamsDTO(flightSearchFilterParamsDTO, size, index,
                sortBy, sortDirection);
        FlightPageQuery query = new FlightPageQuery(flightSearchParamsDTO, departureDate, arrivalDate, departureAirportId,
                arrivalAirportId);
        return Response.ok(flightService.searchFlightsByParams(query)).build();
    }

    @GET
    @Path("multiple-params")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByMultipleParams(@QueryParam("departureAirport") String departureAirport,
                                           @QueryParam("departureDate") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in yyyy-mm-dd format") String departureDate,
                                           @QueryParam("arrivalAirport") String arrivalAirport,
                                           @QueryParam("arrivalDate") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Date must be in yyyy-mm-dd format") String arrivalDate,
                                           @QueryParam("size") @DefaultValue("10") Integer size,
                                           @QueryParam("index") @DefaultValue("0") Integer index){
        FlightMultipleParamsSearchDTO flightMultipleParamsSearchDTO = new FlightMultipleParamsSearchDTO(departureAirport, departureDate, arrivalAirport, arrivalDate);
        return Response.ok(flightService.searchFlightsByMultipleParams(flightMultipleParamsSearchDTO, size, index)).build();
    }

    @POST
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFLight(@Valid FlightCreateRepresentation flightCreateRepresentation) {
        flightService.createFlight(flightCreateRepresentation);
        return Response.ok(new MessageRepresentation(SuccessMessages.FLIGHT_CREATION_SUCCESS)).build();
    }

    @PUT
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{flightId}")
    public Response updateFlight(@PathParam("flightId") Integer id, @Valid FlightDateUpdateRepresentation representation) {
        flightService.updateFlightDates(representation, id);
        return Response.ok(new MessageRepresentation(SuccessMessages.FLIGHT_UPDATE_SUCCESS)).build();
    }

    @PUT
    @RolesAllowed(Role.AIRLINE_ADMINISTRATOR)
    @Path("update-flight-status/{id:[0-9]+}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateFlightStatus(@PathParam("id") Integer id,
                                       @QueryParam("newStatus") @NotNull @EnumerationValue(acceptedEnum = FlightStatus.class)
                                        String newStatus) {
        flightService.updateFlightStatus(id, ValueEnum.fromValue(newStatus, FlightStatus.class));
        return Response.ok(new MessageRepresentation(SuccessMessages.FLIGHT_CANCEL_SUCCESS)).build();
    }

}
