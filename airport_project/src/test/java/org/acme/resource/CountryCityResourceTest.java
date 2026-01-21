package org.acme.resource;

import io.quarkus.test.junit.QuarkusTest;
import org.acme.constant.AirportProjectURIs;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
class CountryCityResourceTest {

    @Test
    void smartSearchCountries() {
        given()
                .queryParam("value", "Gr")
                .when()
                .get(AirportProjectURIs.CITIES_COUNTRIES+"/smart-search/country")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }

    @Test
    void smartSearchCities() {
        given()
                .queryParam("value", "Ath")
                .queryParam("country", "Greece")
                .when()
                .get(AirportProjectURIs.CITIES_COUNTRIES+"/smart-search/city")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1));
    }
}
