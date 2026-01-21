package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.constant.AirportProjectURIs;
import org.acme.persistence.JPATest;
import org.acme.representation.flight.FlightRepresentation;
import org.acme.representation.user.UserRepresentation;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
@Disabled
class PassengerResourceTest extends JPATest {

    static final String API_ROOT  = "http://localhost:8081";

    @Test
    void findAllPassengers() {
        List<UserRepresentation> passengers = when().get(API_ROOT+ AirportProjectURIs.PASSENGERS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<UserRepresentation>>() {});
        assertEquals(2, passengers.size());
    }

    @Test
    void findPassengerByEmail() {
        List<UserRepresentation> passengers = given().queryParam("email", "passenger@gmail.com").when().get(API_ROOT + AirportProjectURIs.PASSENGERS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<UserRepresentation>>() {}) ;

        assertEquals(1, passengers.size());
    }

    @Test
    void findExistingPassenger() {
        UserRepresentation p = when().get(API_ROOT+ AirportProjectURIs.PASSENGERS+"/"+ 6)
                .then()
                .statusCode(200)
                .extract().as(UserRepresentation.class);

        assertEquals("passenger", p.getUsername());
    }

    @Test
    void findNoExistingPassenger() {
        when().get(API_ROOT +AirportProjectURIs.PASSENGERS + "/" + 666)
                .then()
                .statusCode(404);
    }

    @Test
    void findFlights(){

        List<FlightRepresentation> fr = when().get(API_ROOT+ AirportProjectURIs.PASSENGERS+"/searchForFlights/4/Venezuelo")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(1,fr.size());
        assertEquals("Venezuelo",fr.getFirst().getArrivalAirport());
        assertEquals("Fiumicino",fr.getFirst().getDepartureAirport());
    }

    @Test
    void findNoExistingFlight() {
        when().get(API_ROOT+ AirportProjectURIs.PASSENGERS+"/searchForFlights/5/Venezuelo")
                .then()
                .statusCode(404);
    }

    @Test
    @TestTransaction
    void createPassenger(){
        UserRepresentation userRepresentation = new UserRepresentation();
        UserRepresentation savedPassenger = given().contentType(ContentType.JSON).body(userRepresentation).when()
                .post(API_ROOT + AirportProjectURIs.PASSENGERS).then().statusCode(201).extract().as(UserRepresentation.class);

        assertNotNull(savedPassenger);
        assertEquals("passenger123",savedPassenger.getUsername());
        assertEquals("8388383838",savedPassenger.getPhoneNumber());
        assertEquals("email@gmail.com", savedPassenger.getEmail());
    }

    @Test
    void updatePassenger() {
        UserRepresentation passenger = when().get(API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then()
                .statusCode(200)
                .extract().as(UserRepresentation.class);

        passenger.setUsername("passenger123");
        passenger.setEmail("sjdfs@gmail.com");
        passenger.setPhoneNumber("999999999");

        given()
                .contentType(ContentType.JSON)
                .body(passenger)
                .when().put(API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then().statusCode(204);

        UserRepresentation updated = when().get(API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then()
                .statusCode(200)
                .extract().as(UserRepresentation.class);

        assertEquals("passenger123", updated.getUsername());
    }

    @Test
    void updatePassengerWithNotTheSameId() {
        UserRepresentation passenger = when().get(API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then()
                .statusCode(200)
                .extract().as(UserRepresentation.class);

        passenger.setId(10);

        given().contentType(ContentType.JSON).body(passenger)
                .when().put(API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then().statusCode(400);
    }

    @Test
    @TestTransaction
    void removeExistingPassenger(){
        when()
                .delete(API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 12)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestTransaction
    void removeNoExistingPassenger(){
        when()
                .delete(API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 4)
                .then()
                .statusCode(404);
    }

    @Test
    void makeNewReservation(){
        when().post(API_ROOT + AirportProjectURIs.PASSENGERS + "/12/makeReservation/Gkinis/Evangelos/AA4839/FR8438/23/1/F4")
                .then().statusCode(201)
                .header("Location", Matchers.matchesPattern(".*/Reservations/[0-9]+"));
    }

    @Test
    void denyAddingExistingReservations(){
        when()
                .post(API_ROOT + "/6/makeReservation/Wonder/Bob/CP152D45/FR8438/0/0/1A")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void deleteAReservation(){
        when()
                .delete(API_ROOT + AirportProjectURIs.PASSENGERS + "/6/deleteReservation/11")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void denyDeletingNonExistingReservation(){
        when()
                .delete(API_ROOT + AirportProjectURIs.PASSENGERS + "/12/deleteReservation/20")
                .then()
                .statusCode(404);
    }
}
