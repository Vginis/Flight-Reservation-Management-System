package org.acme.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.representation.user.AirlineAdministratorCreateRepresentation;
import org.acme.service.AirlineAdministratorService;

@Path(AirportProjectURIs.AIRLINE_ADMINISTRATORS)
@Authenticated
public class AirlineAdministratorResource {
    @Inject
    AirlineAdministratorService airlineAdministratorService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public Response createAirlineAdministrator(@Valid AirlineAdministratorCreateRepresentation airlineAdministratorCreateRepresentation){
        airlineAdministratorService.createAirlineAdministrator(airlineAdministratorCreateRepresentation);
        return Response.status(Response.Status.CREATED).build();
    }
}
