package org.acme.util;

import org.acme.constant.Role;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserContextTest {

    @InjectMocks
    UserContext userContext;
    @Mock
    JsonWebToken jsonWebToken;

    @Test
    void test_extract_username() {
        when(jsonWebToken.getClaim("preferred_username")).thenReturn("username");
        Assertions.assertEquals("username", userContext.extractUsername());
    }

    @Test
    void test_extract_firstName() {
        when(jsonWebToken.getClaim("given_name")).thenReturn("firstName");
        Assertions.assertEquals("firstName", userContext.extractFirstName());
    }

    @Test
    void test_extract_lastName() {
        when(jsonWebToken.getClaim("family_name")).thenReturn("lastName");
        Assertions.assertEquals("lastName", userContext.extractLastName());
    }

    @Test
    void test_extract_email() {
        when(jsonWebToken.getClaim("email")).thenReturn("email");
        Assertions.assertEquals("email", userContext.extractEmail());
    }

    @Test
    void test_extract_roles() {
        Map<String, Object> realmAccess = new HashMap<>();
        realmAccess.put("roles", List.of(
                "system_admin",
                "passenger",
                "invalid_role"
        ));

        when(jsonWebToken.getClaim("realm_access")).thenReturn(realmAccess);
        Set<String> roles = userContext.extractRoles();

        Assertions.assertEquals(
                Set.of(Role.SYSTEM_ADMIN, Role.PASSENGER),
                roles
        );
    }

}
