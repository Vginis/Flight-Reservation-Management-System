package org.acme.resource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.acme.domain.Flight;
import org.acme.persistence.JPATest;
import org.acme.representation.FlightRepresentation;
import org.acme.representation.PassengerRepresentation;
import org.acme.util.Fixture;
import org.hamcrest.Matchers;
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
    public void findAllPassengers() {
        List<PassengerRepresentation> passengers = when().get(Fixture.API_ROOT+AirportProjectURIs.PASSENGERS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PassengerRepresentation>>() {});
        assertEquals(2, passengers.size());
    }

    @Test
    public void findPassengerByEmail() {
        List<PassengerRepresentation> passengers = given().queryParam("email", "passenger@gmail.com").when().get(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<PassengerRepresentation>>() {}) ;

        assertEquals(1, passengers.size());
    }

    @Test
    public void findExistingPassenger() {
        PassengerRepresentation p = when().get(Fixture.API_ROOT+ AirportProjectURIs.PASSENGERS+"/"+ 6)
                .then()
                .statusCode(200)
                .extract().as(PassengerRepresentation.class);

        assertEquals("passenger", p.username);
    }

    @Test
    public void findNoExistingPassenger() {
        when().get(Fixture.API_ROOT +AirportProjectURIs.PASSENGERS + "/" + 666)
                .then()
                .statusCode(404);
    }

    @Test
    public void findFlights(){

        List<FlightRepresentation> fr = when().get(Fixture.API_ROOT+ AirportProjectURIs.PASSENGERS+"/searchForFlights/4/Venezuelo")
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<FlightRepresentation>>() {});
        assertEquals(1,fr.size());
        assertEquals("Venezuelo",fr.get(0).arrivalAirport);
        assertEquals("Fiumicino",fr.get(0).departureAirport);
    }

    @Test
    public void findNoExistingFlight() {
        when().get(Fixture.API_ROOT+ AirportProjectURIs.PASSENGERS+"/searchForFlights/5/Venezuelo")
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

    @Test
    public void updatePassengerWithNotTheSameId() {
        PassengerRepresentation passenger = when().get(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then()
                .statusCode(200)
                .extract().as(PassengerRepresentation.class);

        passenger.id = 10;

        given().contentType(ContentType.JSON).body(passenger)
                .when().put(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 6)
                .then().statusCode(400);
    }

    @Test
    @TestTransaction
    public void removeExistingPassenger(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 12)
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @TestTransaction
    public void removeNoExistingPassenger(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/" + 4)
                .then()
                .statusCode(404);
    }

    @Test
    public void makeNewReservation(){
        when().post(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/12/makeReservation/A3651")
                .then().statusCode(201)
                .header("Location", Matchers.matchesPattern(".*/Reservations/[0-9]+"));
    }

    @Test
    public void denyAddingExistingReservations(){
        when()
                .post(Fixture.API_ROOT + "/6/makeReservation/FR8438")
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void deleteAReservation(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/6/deleteReservation/11")
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    public void denyDeletingNonExistingReservation(){
        when()
                .delete(Fixture.API_ROOT + AirportProjectURIs.PASSENGERS + "/12/deleteReservation/20")
                .then()
                .statusCode(404);
    }
}
