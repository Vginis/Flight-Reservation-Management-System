package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.persistence.JPATest;
import org.acme.representation.airport.AirportRepresentation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
class AirportResourceTest extends JPATest {

    public static final String API_ROOT  = "http://localhost:8081";

    @Test
    void findAllAirports() {
        List<AirportRepresentation> airports = when().get(API_ROOT + AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {});

        assertEquals(4, airports.size());
    }

    @Test
    void findAirportBy3DCode() {
        List<AirportRepresentation> airports = given().queryParam("code", "ATH").when().get(API_ROOT + AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {});

        assertEquals(1, airports.size());
    }

    @Test
    void findExistingAirport() {
        AirportRepresentation a1 = when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);

        assertEquals(3, a1.getAirportId());
    }

    @Test
    void findNonExistingAirport() {
        when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 4711)
                .then()
                .statusCode(404);
    }

    @Test
    void createAirport() {
        AirportRepresentation airportRepresentation = new AirportRepresentation();
        AirportRepresentation createdAirport = given()
                .contentType(ContentType.JSON)
                .body(airportRepresentation)
                .when()
                .post(API_ROOT + AirportProjectURIs.AIRPORTS)
                .then().statusCode(201)
                .extract().as(AirportRepresentation.class);

        assertEquals("Furina", createdAirport.getAirportName());
        assertEquals("Fontaine",createdAirport.getCity());
        assertEquals("Teyvat",createdAirport.getCountry());
        assertEquals("BEY",createdAirport.getU3digitCode());
//        assertEquals(1, createdAirport.depFlights.size());
//        assertEquals(1, createdAirport.arrFlights.size());

        List<AirportRepresentation> airports = when().get(API_ROOT + AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {});

        //assertEquals(5, airports.size());
    }

   @Test
    void updateAirport() {
        AirportRepresentation airport = when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        airport.setAirportName("Furina_De_Chateau");
       airport.setCity("Cardinale");
       airport.setCountry("Fontaine");
       airport.setU3digitCode("FON");
//       airport.depFlights = new ArrayList<>();
//       airport.arrFlights = new ArrayList<>();

        given()
                .contentType(ContentType.JSON)
                .body(airport)
                .when().put(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then().statusCode(204);

        AirportRepresentation updated = when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);

        assertEquals("Furina_De_Chateau", updated.getAirportName());
    }

    @Test
    void updateAirportWithNotTheSameId() {
        AirportRepresentation airport = when().get(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        airport.setAirportId(10);

        given().contentType(ContentType.JSON).body(airport)
                .when().put(API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then().statusCode(400);
    }

    @Test
    @TestTransaction
    void removeExistingAirport() {
        when()
                .delete(API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 4)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestTransaction
    void removeNonExistingAirport(){
        when()
                .delete(API_ROOT +  AirportProjectURIs.AIRPORTS + "/" + 8)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

}