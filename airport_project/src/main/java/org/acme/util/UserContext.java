package org.acme.util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.constant.Role;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserContext {
    @Inject
    JsonWebToken jsonWebToken;

    public String extractUsername(){
        return jsonWebToken.getClaim("preferred_username");
    }

    public String extractFirstName(){
        return jsonWebToken.getClaim("given_name");
    }

    public String extractLastName(){
        return jsonWebToken.getClaim("family_name");
    }

    public String extractEmail(){
        return jsonWebToken.getClaim("email");
    }

    public Set<String> extractRoles(){
        Map<String, Object> realmAccess = jsonWebToken.getClaim("realm_access");
        List<?> roles = (List<?>) realmAccess.get("roles");
        return roles.stream()
                .map(Object::toString)
                .map(role -> role.replace("\"", ""))
                .filter(Role.ROLES::contains)
                .collect(Collectors.toSet());
    }
}
