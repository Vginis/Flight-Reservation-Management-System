package org.acme.constant;

public class ErrorMessages {

    private ErrorMessages(){
        //intentionally left blank
    }
    public static final String ENTITY_NOT_FOUND = "entity_not_found";
    public static final String INVALID_VALUE = "invalid_value";

    public static final String AIRPORT_EXISTS = "airport_already_exists";

    public static final String AIRLINE_EXISTS = "airline_already_exists";
    public static final String AIRLINE_HAS_SOLE_ADMIN = "airline_has_sole_admin";

    public static final String KEYCLOAK_USER_NOT_CREATED = "keycloak_user_not_created";

    public static final String USER_EXISTS = "user_already_exists";
}
