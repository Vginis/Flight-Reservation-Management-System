package org.acme.resource.rest;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;

@Path(AirportProjectURIs.AIRLINE_ADMINISTRATORS)
@Authenticated
public class AirlineAdministratorResource {
    @Inject
    AirlineAdministratorService airlineAdministratorService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed(Role.SYSTEM_ADMIN)
    public Response createAirlineAdministrator(@RestForm("airlineAdministratorCreateRepresentation") @Valid @PartType(MediaType.APPLICATION_JSON)
                                                   AirlineAdministratorCreateRepresentation airlineAdministratorCreateRepresentation,
                                               @Valid @RestForm("airlineLogo") @NotNull FileUpload airlineLogo) throws IOException {
        airlineAdministratorService.createAirlineAdministrator(airlineAdministratorCreateRepresentation, airlineLogo);
        return Response.status(Response.Status.CREATED).build();
    }
}
