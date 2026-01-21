package org.acme.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.MediaType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.representation.user.AirlineAdministratorCreateRepresentation;
import org.acme.service.AirlineAdministratorService;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@QuarkusTest
class AirlineAdministratorResourceTest {

    @InjectMock
    AirlineAdministratorService airlineAdministratorService;

    AirlineAdministratorCreateRepresentation airlineAdministratorCreateRepresentation;
    @BeforeEach
    void setup() {
        airlineAdministratorCreateRepresentation = UserUtil.createAirlineAdministratorCreateRepresentation();
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = Role.SYSTEM_ADMIN)
    void createAirlineAdministrator_success() throws IOException {
        doNothing().when(airlineAdministratorService)
                .createAirlineAdministrator(any(), any());

        given()
            .contentType(ContentType.MULTIPART)
            .multiPart(
                    "airlineAdministratorCreateRepresentation",
                    airlineAdministratorCreateRepresentation,
                    MediaType.APPLICATION_JSON
            )
            .multiPart(
                    "airlineLogo",
                    "logo.png",
                    "fake-image-content".getBytes(),
                    "image/png"
            )
            .when()
            .post(AirportProjectURIs.AIRLINE_ADMINISTRATORS)
            .then()
            .statusCode(201);

        Mockito.verify(airlineAdministratorService)
                .createAirlineAdministrator(any(), any());
    }
}
