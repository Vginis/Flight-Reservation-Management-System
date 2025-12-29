package org.acme.resource.rest;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.service.WebSocketSessionService;

@Path(AirportProjectURIs.WS_SESSION)
@Authenticated
public class WebSocketSessionResource {

    @Inject
    WebSocketSessionService webSocketSessionService;

    @POST
    @Path("/{flightUUID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSession(@PathParam("flightUUID") String flightUUID) {
        return Response.ok(webSocketSessionService.createSession(flightUUID)).build();
    }
}
