package org.acme.resourceTest;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

import org.acme.persistence.JPATest;
import org.acme.resource.*;

import org.acme.representation.AirportRepresentation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class AirportResourceTest extends JPATest {

    public static String API_ROOT  = "http://localhost:8081";
    public static Integer UML_USER_GUIDE_ID = 1;

    @Test
    public void find() {
        AirportRepresentation a1 = when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + UML_USER_GUIDE_ID)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        Assertions.assertEquals("ATH", a1.u3digitCode);
    }
}