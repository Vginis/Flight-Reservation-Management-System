package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.jwt.JsonWebToken;

@ApplicationScoped
public class UserContext {
    @Inject
    JsonWebToken jsonWebToken;

    public String extractUsername(){
        return jsonWebToken.getClaim("preferred_username");
    }
}
