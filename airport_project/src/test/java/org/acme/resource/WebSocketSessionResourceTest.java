package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
class WebSocketSessionResourceTest {

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void testCreateSession() {
        given()
            .when()
            .post(AirportProjectURIs.WS_SESSION+"/bf16bdd1-cdb1-4949-9428-264fd22211cd")
            .then()
            .statusCode(200);
    }

    @Test
    void testCreateSession_returns_401() {
        given()
            .when()
            .post(AirportProjectURIs.WS_SESSION+"/bf16bdd1-cdb1-4949-9428-264fd22211cd")
            .then()
            .statusCode(401);
    }
}
