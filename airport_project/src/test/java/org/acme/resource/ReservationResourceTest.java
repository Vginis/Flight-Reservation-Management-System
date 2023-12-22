package org.acme.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import org.acme.persistence.JPATest;
import org.acme.representation.ReservationRepresentation;
import org.acme.util.Fixture;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class ReservationResourceTest extends JPATest {

    /*@Test
    public void find() {
        ReservationRepresentation a = when().get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS + "/" + Fixture.Reservations.RESERVATION_ONE_WAY_ID)
                .then()
                .statusCode(200)
                .extract().as(ReservationRepresentation.class);
        assertEquals(null, a.passengerId);
    }*/

    @Test
    public void searchAllReservations() throws JsonMappingException, JsonProcessingException {
        List<ReservationRepresentation> reservations = when()
                .get(Fixture.API_ROOT + AirportProjectURIs.RESERVATIONS)
                .then()
                .statusCode(200)
                .extract().as(new TypeRef<List<ReservationRepresentation>>() {});
        assertEquals(2, reservations.size());

    }

}
