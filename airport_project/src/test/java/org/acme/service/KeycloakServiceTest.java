package org.acme.service;

import jakarta.ws.rs.core.Response;
import org.acme.constant.KeycloakConfiguration;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.representation.user.UserCreateRepresentation;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.*;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    private static final int CHANGE_PASSWORD_EMAIL_SECONDS_LIFESPAN = 172800;

    @InjectMocks
    KeycloakService keycloakService;
    @Mock
    KeycloakConfiguration keycloakConfiguration;
    @Mock
    KeycloakBuilder keycloakBuilder;
    @Mock
    Keycloak keycloak;
    @Mock
    RealmResource realmResource;
    @Mock
    UsersResource usersResource;
    @Mock
    UserResource userResource;
    @Mock
    Response response;
    @Mock
    RolesResource rolesResource;
    @Mock
    RoleResource roleResource;
    @Mock
    RoleMappingResource roleMappingResource;
    @Mock
    RoleScopeResource roleScopeResource;

    UserCreateRepresentation userCreateRepresentation;
    @BeforeEach
    void setup(){
        userCreateRepresentation = UserUtil.createAirlineAdministratorCreateRepresentation();
    }

    @Test
    void test_initialize_keycloak(){
        Mockito.when(keycloakConfiguration.getKeycloakServerURL())
                .thenReturn("keycloak-server-url");
        Mockito.when(keycloakConfiguration.getKeycloakAdminRealm())
                .thenReturn("admin-realm");
        Mockito.when(keycloakConfiguration.getKeycloakAdminClientId())
                .thenReturn("admin-client-id");
        Mockito.when(keycloakConfiguration.getKeycloakAdminGrantType())
                .thenReturn("password");
        Mockito.when(keycloakConfiguration.getKeycloakAdminUsername())
                .thenReturn("admin-username");
        Mockito.when(keycloakConfiguration.getKeycloakAdminPassword())
                .thenReturn("admin-password");

        Mockito.mockStatic(KeycloakBuilder.class);
        Mockito.when(KeycloakBuilder.builder()).thenReturn(keycloakBuilder);
        Mockito.when(keycloakBuilder.serverUrl(Mockito.anyString())).thenReturn(keycloakBuilder);
        Mockito.when(keycloakBuilder.realm(Mockito.anyString())).thenReturn(keycloakBuilder);
        Mockito.when(keycloakBuilder.clientId(Mockito.anyString())).thenReturn(keycloakBuilder);
        Mockito.when(keycloakBuilder.grantType(Mockito.anyString())).thenReturn(keycloakBuilder);
        Mockito.when(keycloakBuilder.username(Mockito.anyString())).thenReturn(keycloakBuilder);
        Mockito.when(keycloakBuilder.password(Mockito.anyString())).thenReturn(keycloakBuilder);
        Mockito.when(keycloakBuilder.build()).thenReturn(keycloak);
        Assertions.assertDoesNotThrow(() -> keycloakService.initializeKeycloak());
    }

    @Test
    void test_close_keycloak(){
        Mockito.doNothing().when(keycloak).close();
        Assertions.assertDoesNotThrow(() -> keycloakService.closeKeycloak());
    }

    @Test
    void test_create_keycloak_user(){
        Mockito.when(keycloakConfiguration.getKeycloakAppRealm())
                .thenReturn("airport");
        Mockito.when(keycloak.realm("airport")).thenReturn(realmResource);
        Mockito.when(realmResource.users()).thenReturn(usersResource);
        Mockito.when(usersResource.search("user-1")).thenReturn(List.of());
        Mockito.when(usersResource.create(Mockito.any(UserRepresentation.class)))
                .thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(201);
        Mockito.when(response.getLocation()).thenReturn(URI.create("http://localhost/user-id"));
        Mockito.when(realmResource.roles()).thenReturn(rolesResource);
        Mockito.when(rolesResource.get("role")).thenReturn(roleResource);
        Mockito.when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation());
        Mockito.when(usersResource.get("user-id")).thenReturn(userResource);
        Mockito.when(userResource.roles()).thenReturn(roleMappingResource);
        Mockito.when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        Mockito.doNothing().when(roleScopeResource).add(Mockito.anyList());
        Mockito.doNothing().when(userResource).executeActionsEmail(List.of("UPDATE_PASSWORD"),
                CHANGE_PASSWORD_EMAIL_SECONDS_LIFESPAN);

        Assertions.assertDoesNotThrow(() -> keycloakService.createKeycloakUser(userCreateRepresentation,"role"));
    }

    @Test
    void test_create_keycloak_user_failure_to_send_email(){
        Mockito.when(keycloakConfiguration.getKeycloakAppRealm())
                .thenReturn("airport");
        Mockito.when(keycloak.realm("airport")).thenReturn(realmResource);
        Mockito.when(realmResource.users()).thenReturn(usersResource);
        Mockito.when(usersResource.search("user-1")).thenReturn(List.of());
        Mockito.when(usersResource.create(Mockito.any(UserRepresentation.class)))
                .thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(201);
        Mockito.when(response.getLocation()).thenReturn(URI.create("http://localhost/user-id"));
        Mockito.when(realmResource.roles()).thenReturn(rolesResource);
        Mockito.when(rolesResource.get("role")).thenReturn(roleResource);
        Mockito.when(roleResource.toRepresentation()).thenReturn(new RoleRepresentation());
        Mockito.when(usersResource.get("user-id")).thenReturn(userResource);
        Mockito.when(userResource.roles()).thenReturn(roleMappingResource);
        Mockito.when(roleMappingResource.realmLevel()).thenReturn(roleScopeResource);
        Mockito.doNothing().when(roleScopeResource).add(Mockito.anyList());
        Mockito.doThrow(RuntimeException.class).when(userResource).executeActionsEmail(List.of("UPDATE_PASSWORD"),
                CHANGE_PASSWORD_EMAIL_SECONDS_LIFESPAN);

        Assertions.assertDoesNotThrow(() -> keycloakService.createKeycloakUser(userCreateRepresentation,"role"));
    }

    @Test
    void test_create_keycloak_user_failure_to_create_user(){
        Mockito.when(keycloakConfiguration.getKeycloakAppRealm())
                .thenReturn("airport");
        Mockito.when(keycloak.realm("airport")).thenReturn(realmResource);
        Mockito.when(realmResource.users()).thenReturn(usersResource);
        Mockito.when(usersResource.search("user-1")).thenReturn(List.of());
        Mockito.when(usersResource.create(Mockito.any(UserRepresentation.class)))
                .thenReturn(response);
        Mockito.when(response.getStatus()).thenReturn(502);

        Assertions.assertThrows(InvalidRequestException.class,
                () -> keycloakService.createKeycloakUser(userCreateRepresentation,"role"));
    }

    @Test
    void test_create_keycloak_user_already_exists(){
        Mockito.when(keycloakConfiguration.getKeycloakAppRealm())
                .thenReturn("airport");
        Mockito.when(keycloak.realm("airport")).thenReturn(realmResource);
        Mockito.when(realmResource.users()).thenReturn(usersResource);
        Mockito.when(usersResource.search("user-1")).thenReturn(List.of(new UserRepresentation()));

        Assertions.assertThrows(InvalidRequestException.class,
                () -> keycloakService.createKeycloakUser(userCreateRepresentation,"role"));
    }

    @Test
    void test_update_user_password(){
        Mockito.when(keycloakConfiguration.getKeycloakAppRealm())
                .thenReturn("airport");
        Mockito.when(keycloak.realm("airport")).thenReturn(realmResource);
        Mockito.when(realmResource.users()).thenReturn(usersResource);
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("user-id");
        Mockito.when(usersResource.search("user-1")).thenReturn(List.of(userRepresentation));
        Mockito.when(usersResource.get("user-id")).thenReturn(userResource);

        Mockito.doNothing().when(userResource).resetPassword(Mockito.any(CredentialRepresentation.class));
        Assertions.assertDoesNotThrow(() -> keycloakService.updateUserPassword("user-1","new Password"));
    }

    @Test
    void test_update_user_password_not_found_exception(){
        Mockito.when(keycloakConfiguration.getKeycloakAppRealm())
                .thenReturn("airport");
        Mockito.when(keycloak.realm("airport")).thenReturn(realmResource);
        Mockito.when(realmResource.users()).thenReturn(usersResource);
        Mockito.when(usersResource.search("user-1")).thenReturn(List.of());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> keycloakService.updateUserPassword("user-1","new Password"));
    }
}
