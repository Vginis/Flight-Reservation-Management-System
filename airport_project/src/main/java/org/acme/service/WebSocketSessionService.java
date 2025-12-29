package org.acme.service;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.representation.wssession.WsSessionResponse;
import org.acme.util.WebSocketSessionStore;

@ApplicationScoped
public class WebSocketSessionService {

    @Inject
    SecurityIdentity securityIdentity;
    @Inject
    WebSocketSessionStore webSocketSessionStore;

    public WsSessionResponse createSession(String flightUUID) {
        String userId = securityIdentity.getPrincipal().getName();
        String sessionId = webSocketSessionStore.createSession(userId, flightUUID);
        return new WsSessionResponse(sessionId);
    }
}
