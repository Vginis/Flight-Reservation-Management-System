package org.acme.resource.rest;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.Role;
import org.acme.constant.ValueEnum;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.constant.search.SortDirection;
import org.acme.representation.reservation.ReservationCreateRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.SortBy;
import org.acme.service.ReservationService;
import org.acme.validation.EnumerationValue;

import static org.acme.constant.AirportProjectURIs.RESERVATIONS;

@Path(RESERVATIONS)
@Authenticated
public class ReservationResource {

    @Inject
    ReservationService reservationService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createReservation(@Valid ReservationCreateRepresentation reservationCreateRepresentation) {
        reservationService.createReservation(reservationCreateRepresentation);
        return Response.ok().build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchByParams(@QueryParam("size") @DefaultValue("10") Integer size,
                                   @QueryParam("index") @DefaultValue("0") Integer index,
                                   @QueryParam("sortDirection") @EnumerationValue(acceptedEnum = SortDirection.class) String sortDirection){
        PageQuery<SortBy> query = new PageQuery<>(size, index, ValueEnum.fromValue(sortDirection, SortDirection.class));
        return Response.ok(reservationService.searchUsersReservations(query)).build();
    }

}
