package org.acme.constant;

import jakarta.enterprise.context.Dependent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
@Dependent
public class KeycloakConfiguration {
    @ConfigProperty(name = "quarkus.keycloak.admin-client.server-url")
    private String keycloakServerURL;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.realm")
    private String keycloakAdminRealm;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.client-id")
    private String keycloakAdminClientId;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.grant-type")
    private String keycloakAdminGrantType;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.username")
    private String keycloakAdminUsername;

    @ConfigProperty(name = "quarkus.keycloak.admin-client.password")
    private String keycloakAdminPassword;

    @ConfigProperty(name = "airport.keycloak.app.realm")
    private String keycloakAppRealm;

    public String getKeycloakServerURL() {
        return keycloakServerURL;
    }

    public String getKeycloakAdminRealm() {
        return keycloakAdminRealm;
    }

    public String getKeycloakAdminClientId() {
        return keycloakAdminClientId;
    }

    public String getKeycloakAdminGrantType() {
        return keycloakAdminGrantType;
    }

    public String getKeycloakAdminUsername() {
        return keycloakAdminUsername;
    }

    public String getKeycloakAdminPassword() {
        return keycloakAdminPassword;
    }

    public String getKeycloakAppRealm() {
        return keycloakAppRealm;
    }
}
