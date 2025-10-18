package org.acme.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.representation.MessageRepresentation;
import org.acme.representation.passenger.PassengerUpdateRepresentation;
import org.acme.representation.user.PassengerCreateRepresentation;
import org.acme.service.PassengerService;

import static org.acme.constant.AirportProjectURIs.PASSENGERS;

@Path(PASSENGERS)
@Authenticated
public class PassengerResource {

    @Inject
    PassengerService passengerService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response createPassenger(@Valid PassengerCreateRepresentation passengerCreateRepresentation) {
        passengerService.createPassenger(passengerCreateRepresentation);
        return Response.ok(new MessageRepresentation(SuccessMessages.PASSENGER_CREATE_SUCCESS)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public Response getPassengerPassport(@QueryParam("username") String username){
        return Response.ok(passengerService.getPassport(username)).build();
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePassenger(@QueryParam("username") String username, PassengerUpdateRepresentation representation) {
        passengerService.updatePassenger(representation, username);
        return Response.ok(new MessageRepresentation(SuccessMessages.PASSENGER_UPDATE_SUCCESS)).build();
    }
}
