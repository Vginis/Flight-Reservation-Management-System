package org.acme.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.constant.search.SortDirection;
import org.acme.profile.IsolatedDbProfile;
import org.acme.representation.reservation.ReservationCreateRepresentation;
import org.acme.util.ReservationUtil;
import org.acme.util.UserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
@TestProfile(IsolatedDbProfile.class)
class ReservationResourceTest {

    @InjectMock
    UserContext userContext;

    ReservationCreateRepresentation reservationCreateRepresentation;
    @BeforeEach
    void setup() {
        reservationCreateRepresentation = ReservationUtil.createReservationCreateRepresentation();
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_searchReservations() {
        Mockito.when(userContext.extractUsername()).thenReturn("passenger1");
        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
            .queryParam("sortDirection", SortDirection.DESCENDING.value())
            .when()
            .get(AirportProjectURIs.RESERVATIONS)
            .then()
            .statusCode(200)
            .body("results.size()", equalTo(2));
    }

    @Test
    void test_searchReservations_returns_401() {
        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
            .queryParam("sortDirection", SortDirection.DESCENDING.value())
            .when()
            .get(AirportProjectURIs.RESERVATIONS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestTransaction
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_createReservation_success() {
        Mockito.when(userContext.extractUsername()).thenReturn("passenger1");
        given()
            .contentType(ContentType.JSON)
            .body(reservationCreateRepresentation)
            .when()
            .post(AirportProjectURIs.RESERVATIONS)
            .then()
            .statusCode(200);
    }
}
