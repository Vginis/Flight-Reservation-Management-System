package org.acme.Quarkus_Testing_Service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

//@QuarkusTest
class GreetingResourceTest {
   // @Test
    void testHelloEndpoint() {
        given()
          .when().get("/hello")
          .then()
             .statusCode(200)
             .body(is("Hello from RESTEasy Reactive"));
    }

}