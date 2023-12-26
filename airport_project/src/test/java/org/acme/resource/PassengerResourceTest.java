package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.acme.persistence.JPATest;
import org.acme.representation.PassengerRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class PassengerResourceTest extends JPATest {
    @Test
    public void findPassenger() {
        PassengerRepresentation p = when().get(Fixture.API_ROOT+ AirportProjectURIs.PASSENGERS+"/"+ 6)
                .then()
                .statusCode(200)
                .extract().as(PassengerRepresentation.class);
        assertEquals("passenger", p.username);
    }

    @Test
    public void search() throws JsonMappingException, JsonProcessingException {


        List<PassengerRepresentation> passengers = given().queryParam("email", "passenger@gmail.com").when().get(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PassengerRepresentation>>() {}) ;

        Assertions.assertEquals(1, passengers.size());

    }

    @Test
    public void findNonExisting() {
        when().get(Fixture.API_ROOT +AirportProjectURIs.PASSENGERS + "/" + 666)
                .then()
                .statusCode(404);
    }

    @Test
    @TestTransaction
    public void createPassenger(){
        PassengerRepresentation passengerRepresentation = Fixture.getPassengerRepresentation();
        PassengerRepresentation savedPassenger = given().contentType(ContentType.JSON).body(passengerRepresentation).when()
                .post(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS).then().statusCode(201).extract().as(PassengerRepresentation.class);

        assertNotNull(savedPassenger);
        assertEquals("passenger123",savedPassenger.username);
        assertEquals("AK810399",savedPassenger.passport_id);
        assertEquals("8388383838",savedPassenger.phoneNum);
        assertEquals("email@gmail.com", savedPassenger.email);
        assertEquals("VGinis12@djsj", savedPassenger.password);
        assertNotNull(savedPassenger.reservationsId);
    }

    @Test
    public void updatePassenger() {
        PassengerRepresentation passenger = when().get(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then()
                .statusCode(200)
                .extract().as(PassengerRepresentation.class);

        passenger.username = "passenger123";
        passenger.password = "jjdsfUJ23$";
        passenger.email = "sjdfs@gmail.com";
        passenger.phoneNum = "999999999";
        passenger.passport_id ="AK48597";
        passenger.reservationsId = new ArrayList<>();

        given()
                .contentType(ContentType.JSON)
                .body(passenger)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then().statusCode(204);


        PassengerRepresentation updated = when().get(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then()
                .statusCode(200)
                .extract().as(PassengerRepresentation.class);

        assertEquals("passenger123", updated.username);
    }
}
