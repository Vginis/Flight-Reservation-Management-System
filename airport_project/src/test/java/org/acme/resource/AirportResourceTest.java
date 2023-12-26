package org.acme.resource;

import org.acme.persistence.JPATest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.TestTransaction;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.acme.representation.AirportRepresentation;
import org.acme.util.Fixture;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class AirportResourceTest extends JPATest {
    @Test
    public void findAirportById() {
        AirportRepresentation a1 = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        assertEquals(3, a1.airportId);
    }

    @Test
    public void searchAirportById() throws JsonMappingException, JsonProcessingException {


        List<AirportRepresentation> airports = given().queryParam("airportId", 3).when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {});

        assertEquals(4, airports.size());

    }

    @Test
    public void findNonExistingAirport() {
        when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 4711)
                .then()
                .statusCode(404);
    }
    //TODO Κάποια στιγμή αφού έχουμε τελειώσει να δούμε το airportId
    @Test
    public void createAirport() {

        AirportRepresentation airportRepresentation = Fixture.getAirportRepresentation();
        airportRepresentation.airportId = 1;
        AirportRepresentation createdAirport = given().contentType(ContentType.JSON).body(airportRepresentation).when()
                .post(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS).then().statusCode(201)
                .extract().as(AirportRepresentation.class);

        assertEquals("Furina", createdAirport.airportName);
    }


   @Test
    public void updateAirport() {
        AirportRepresentation airport = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);

        airport.airportName = "Furina_De_Chateau";

        given()
                .contentType(ContentType.JSON)
                .body(airport)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then().statusCode(204);


        AirportRepresentation updated = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);

        assertEquals("Furina_De_Chateau", updated.airportName);
    }

    @Test
    @TestTransaction
    public void removeExistingAirport() {

        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 4)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestTransaction
    void removeNonExistingAirport(){

        when()
                .delete(Fixture.API_ROOT +  AirportProjectURIs.AIRPORTS + "/" + 8)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}