package org.acme.resource;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.acme.constant.AirportProjectURIs;
import org.acme.constant.Role;
import org.acme.constant.SuccessMessages;
import org.acme.profile.IsolatedDbProfile;
import org.acme.representation.user.PasswordResetRepresentation;
import org.acme.representation.user.UserCreateRepresentation;
import org.acme.representation.user.UserUpdateRepresentation;
import org.acme.service.UserService;
import org.acme.util.UserContext;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@QuarkusTest
@TestProfile(IsolatedDbProfile.class)
class UserResourceTest {

    @InjectMock
    UserService userService;
    @InjectMock
    UserContext userContext;

    UserCreateRepresentation userCreateRepresentation;
    PasswordResetRepresentation passwordResetRepresentation;
    UserUpdateRepresentation userUpdateRepresentation;
    @BeforeEach
    void setup() {
        userCreateRepresentation = UserUtil.createUserCreateRepresentation();
        passwordResetRepresentation = new PasswordResetRepresentation();
        passwordResetRepresentation.setNewPassword("Password123!");
        userUpdateRepresentation = UserUtil.createUserUpdateRepresentation();
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_searchUsers() {

        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
            .when()
            .get(AirportProjectURIs.USERS)
            .then()
            .statusCode(200);
    }

    @Test
    void test_searchUsers_returns_401() {
        given()
            .queryParam("size", 10)
            .queryParam("index", 0)
            .when()
            .get(AirportProjectURIs.USERS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_createSystemAdministrator() {
        Mockito.doNothing().when(userService).createSystemAdministrator(userCreateRepresentation);
        given()
            .contentType(ContentType.JSON)
            .body(userCreateRepresentation)
            .when()
            .post(AirportProjectURIs.USERS)
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.SYSTEM_ADMIN_CREATE_SUCCESS));
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_createSystemAdministrator_returns_403() {
        given()
            .contentType(ContentType.JSON)
            .body(userCreateRepresentation)
            .when()
            .post(AirportProjectURIs.USERS)
            .then()
            .statusCode(403);
    }

    @Test
    void test_createSystemAdministrator_returns_401() {
        given()
            .contentType(ContentType.JSON)
            .body(userCreateRepresentation)
            .when()
            .post(AirportProjectURIs.USERS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_resetPassword_success() {
        Mockito.doNothing().when(userService).resetPassword(passwordResetRepresentation);
        given()
            .contentType(ContentType.JSON)
            .body(passwordResetRepresentation)
            .when()
            .put(AirportProjectURIs.USERS+"/password-reset")
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.PASSWORD_RESET_SUCCESS));
    }

    @Test
    void test_resetPassword_returns_401() {
        given()
            .contentType(ContentType.JSON)
            .body(passwordResetRepresentation)
            .when()
            .put(AirportProjectURIs.USERS+"/password-reset")
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_getUserProfile_success() {
        Mockito.when(userContext.extractUsername()).thenReturn("passenger1");
        given()
            .when()
            .get(AirportProjectURIs.USERS+"/user-profile")
            .then()
            .statusCode(200);
    }

    @Test
    void test_getUserProfile_returns_401() {
        given()
            .when()
            .get(AirportProjectURIs.USERS+"/user-profile")
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "passenger", roles = {Role.PASSENGER})
    void test_updateUserProfile_success() {
        Mockito.when(userContext.extractUsername()).thenReturn("passenger1");
        given()
                .contentType(ContentType.JSON)
                .body(userUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.USERS+"/user-profile")
                .then()
                .statusCode(200);
    }

    @Test
    void test_updateUserProfile_returns_401() {
        given()
                .contentType(ContentType.JSON)
                .body(userUpdateRepresentation)
                .when()
                .put(AirportProjectURIs.USERS+"/user-profile")
                .then()
                .statusCode(401);
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_updateUserDetails_success() {
        given()
            .contentType(ContentType.JSON)
            .body(userUpdateRepresentation)
            .queryParam("username", "passenger1")
            .when()
            .put(AirportProjectURIs.USERS)
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.USER_UPDATE_SUCCESS));
    }

    @Test
    void test_updateUserDetails_return_401() {
        given()
            .contentType(ContentType.JSON)
            .body(userUpdateRepresentation)
            .queryParam("username", "passenger1")
            .when()
            .put(AirportProjectURIs.USERS)
            .then()
            .statusCode(401);
    }

    @Test
    @TestSecurity(user = "sys_admin", roles = {Role.SYSTEM_ADMIN})
    void test_deleteUser_success() {
        given()
            .when()
            .delete(AirportProjectURIs.USERS+"/1")
            .then()
            .statusCode(200)
            .body("key", equalTo(SuccessMessages.USER_DELETE_SUCCESS));
    }

    @Test
    void test_deleteUser_success_returns401() {
        given()
                .when()
                .delete(AirportProjectURIs.USERS+"/1")
                .then()
                .statusCode(401);
    }
}
