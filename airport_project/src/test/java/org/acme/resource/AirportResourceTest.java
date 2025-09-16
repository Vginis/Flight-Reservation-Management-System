package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.persistence.JPATest;
import org.acme.representation.airport.AirportRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;


@QuarkusTest
public class AirportResourceTest extends JPATest {

    @Test
    public void findAllAirports() {
        List<AirportRepresentation> airports = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {});

        assertEquals(4, airports.size());
    }

    @Test
    public void findAirportBy3DCode() {
        List<AirportRepresentation> airports = given().queryParam("code", "ATH").when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {});

        assertEquals(1, airports.size());
    }

    @Test
    public void findExistingAirport() {
        AirportRepresentation a1 = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);

        assertEquals(3, a1.airportId);
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
        AirportRepresentation createdAirport = given()
                .contentType(ContentType.JSON)
                .body(airportRepresentation)
                .when()
                .post(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS)
                .then().statusCode(201)
                .extract().as(AirportRepresentation.class);

        assertEquals("Furina", createdAirport.airportName);
        assertEquals("Fontaine",createdAirport.city);
        assertEquals("Teyvat",createdAirport.country);
        assertEquals("BEY",createdAirport.u3digitCode);
//        assertEquals(1, createdAirport.depFlights.size());
//        assertEquals(1, createdAirport.arrFlights.size());

        List<AirportRepresentation> airports = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirportRepresentation>>() {});

        //assertEquals(5, airports.size());
    }

   @Test
    public void updateAirport() {
        AirportRepresentation airport = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        airport.airportName = "Furina_De_Chateau";
       airport.city = "Cardinale";
       airport.country = "Fontaine";
       airport.u3digitCode = "FON";
//       airport.depFlights = new ArrayList<>();
//       airport.arrFlights = new ArrayList<>();

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
    public void updateAirportWithNotTheSameId() {
        AirportRepresentation airport = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRPORTS + "/" + 3)
                .then()
                .statusCode(200)
                .extract().as(AirportRepresentation.class);
        airport.airportId = 10;

        given().contentType(ContentType.JSON).body(airport)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + 5)
                .then().statusCode(400);
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