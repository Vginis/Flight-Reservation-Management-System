package org.acme.constant;

public class ErrorMessages {

    private ErrorMessages(){
        //intentionally left blank
    }
    public static final String ENTITY_NOT_FOUND = "Entity was not found in the system.";
    public static final String INVALID_VALUE = "Invalid value.";

    public static final String AIRCRAFT_HAS_PENDING_FLIGHTS = "An aircraft that has Flights pending can't be erased from the system";

    public static final String AIRPORT_EXISTS = "Airport already exists in the system";

    public static final String AIRLINE_EXISTS = "Airline already exists in the system.";
    public static final String AIRLINE_HAS_SOLE_ADMIN = "The user that is about to be deleted, is the sole administrator" +
            " of the current airline.";

    public static final String KEYCLOAK_USER_NOT_CREATED = "Keycloak user was not created.";

    public static final String USER_EXISTS = "User already exists in the system.";
    public static final String USER_NOT_DELETED = "User was not deleted successfully from the system.s";
}
