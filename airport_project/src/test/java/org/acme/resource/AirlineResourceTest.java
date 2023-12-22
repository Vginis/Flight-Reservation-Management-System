package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Airline;
import org.acme.persistence.JPATest;
import org.acme.representation.AirlineRepresentation;
import org.acme.representation.AirportRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class AirlineResourceTest extends JPATest {
    @Test
    public void findAirline() {
        AirlineRepresentation a1 = when().get(Fixture.API_ROOT+ AirportProjectURIs.AIRLINES+"/"+ 5)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);
        assertEquals("FR", a1.u2digitCode);
    }

    @Test
    public void searchAirline() throws JsonMappingException, JsonProcessingException {


        List<AirlineRepresentation> airlines = given().queryParam("name", "Aegean Airlines").when().get(Fixture.API_ROOT+AirportProjectURIs.AIRLINES)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<AirlineRepresentation>>() {}) ;

        assertEquals(1, airlines.size());

    }

    @Test
    public void submitAirline(){
        AirlineRepresentation airlineRepresentation = Fixture.getAirlineRepresentation();
        AirlineRepresentation savedAirline = given().contentType(ContentType.JSON).body(airlineRepresentation).when()
                                            .post(Fixture.API_ROOT + "/Airlines").then().statusCode(201).extract().as(AirlineRepresentation.class);

        assertNotNull(savedAirline);
        assertEquals("British Airways", savedAirline.name);

    }

    @Test
    public void updateAirline() {
        AirlineRepresentation airline = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + Fixture.Airlines.AIRLINE_ID)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);
        airline.name = "Pao Airlines";


        given()
                .contentType(ContentType.JSON)
                .body(airline)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + Fixture.Airlines.AIRLINE_ID)
                .then().statusCode(204);


        AirlineRepresentation updated = when().get(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/" + Fixture.Airlines.AIRLINE_ID)
                .then()
                .statusCode(200)
                .extract().as(AirlineRepresentation.class);

        assertEquals("Pao Airlines", updated.name);
    }

    @Test
    @TestTransaction
    public void removeExistingAirline(){

        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.AIRLINES + "/9")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

}
