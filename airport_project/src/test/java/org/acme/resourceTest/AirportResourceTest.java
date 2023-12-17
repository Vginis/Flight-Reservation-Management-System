package org.acme.resourceTest;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;
import java.util.List;
import org.acme.resource.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import org.acme.IntegrationBase;
import org.acme.representation.AirportRepresentation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;

@QuarkusTest
public class AirportResourceTest extends IntegrationBase {

    public static String API_ROOT  = "http://localhost:8081";
    public static Integer UML_USER_GUIDE_ID = 30;

    @Test
    public void find() {
        AirportRepresentation a1 = when().get(API_ROOT + ConvergenceUri.AIRPORTS + "/" + UML_USER_GUIDE_ID)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        Assertions.assertEquals(UML_USER_GUIDE_ID, a1.airportId);
    }
}