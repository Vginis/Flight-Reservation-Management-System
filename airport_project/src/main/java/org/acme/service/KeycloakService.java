package org.acme.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.acme.constant.ErrorMessages;
import org.acme.constant.KeycloakConfiguration;
import org.acme.exception.ErrorResponse;
import org.acme.exception.InvalidRequestException;
import org.acme.representation.user.UserCreateRepresentation;
import org.jboss.logging.Logger;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.net.URI;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class KeycloakService {

    private Keycloak keycloak;
    private static final int CHANGE_PASSWORD_EMAIL_SECONDS_LIFESPAN = 172800;
    private static final Logger log = Logger.getLogger(KeycloakService.class);

    @Inject
    KeycloakConfiguration keycloakConfiguration;

    @PostConstruct
    public void initializeKeycloak(){
        keycloak = KeycloakBuilder.builder()
                .serverUrl(keycloakConfiguration.getKeycloakServerURL())
                .realm(keycloakConfiguration.getKeycloakAdminRealm())
                .clientId(keycloakConfiguration.getKeycloakAdminClientId())
                .grantType(keycloakConfiguration.getKeycloakAdminGrantType())
                .username(keycloakConfiguration.getKeycloakAdminUsername())
                .password(keycloakConfiguration.getKeycloakAdminPassword())
                .build();
    }

    @PreDestroy
    public void closeKeycloak() {
        keycloak.close();
    }

    public void createKeycloakUser(UserCreateRepresentation userCreateRepresentation, String role) {
        List<UserRepresentation> existingUsers = keycloak.realm(keycloakConfiguration.getKeycloakAppRealm())
                .users().search(userCreateRepresentation.getUsername());

        if(!existingUsers.isEmpty()){
            throw new InvalidRequestException(new ErrorResponse(400, ErrorMessages.USER_EXISTS));
        }

        UserRepresentation keycloakUserRepresentation = constructKeycloakUserRepresentation(userCreateRepresentation);
        String userId;
        try (Response response = keycloak.realm(keycloakConfiguration.getKeycloakAppRealm())
                .users()
                .create(keycloakUserRepresentation)) {

            if (response.getStatus() != 201) {
                throw new InvalidRequestException(new ErrorResponse(400, ErrorMessages.KEYCLOAK_USER_NOT_CREATED));
            }
            userId = extractUserIdFromURI(response.getLocation());
        }

        if(userId!=null){
            assignRoleToUser(userId, role);
            sendResetPasswordEmail(userId);
        }
    }

    private UserRepresentation constructKeycloakUserRepresentation(UserCreateRepresentation userCreateRepresentation){
        UserRepresentation keycloakUserRepresentation = new UserRepresentation();
        keycloakUserRepresentation.setUsername(userCreateRepresentation.getUsername());
        keycloakUserRepresentation.setFirstName(userCreateRepresentation.getFirstName());
        keycloakUserRepresentation.setLastName(userCreateRepresentation.getLastName());
        keycloakUserRepresentation.setEmail(userCreateRepresentation.getEmail());
        keycloakUserRepresentation.setEmailVerified(true);
        keycloakUserRepresentation.setEnabled(true);
        return keycloakUserRepresentation;
    }

    private String extractUserIdFromURI(URI uri){
        String[] segments = uri.getPath().split("/");
        return segments[segments.length-1];
    }

    private void assignRoleToUser(String userId, String role){
        RoleRepresentation roleRepresentation = keycloak.realm(keycloakConfiguration.getKeycloakAppRealm())
                .roles()
                .get(role)
                .toRepresentation();
        keycloak.realm(keycloakConfiguration.getKeycloakAppRealm()).users()
                .get(userId).roles().realmLevel().add(Collections.singletonList(roleRepresentation));
    }

    private void sendResetPasswordEmail(String userId){
        try {
            UserResource user = keycloak.realm(keycloakConfiguration.getKeycloakAppRealm()).users().get(userId);
            user.executeActionsEmail(List.of("UPDATE_PASSWORD"), CHANGE_PASSWORD_EMAIL_SECONDS_LIFESPAN);
        } catch (RuntimeException e) {
            log.errorf("Failed to send 'Update your password' email to user with keycloak id :%s", userId);
        }
    }
}
