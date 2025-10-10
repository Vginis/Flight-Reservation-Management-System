package org.acme.constant;

import java.util.List;

public class Role {

    private Role(){
        //Intentionally left blank
    }

    public static final String SYSTEM_ADMIN = "system_admin";
    public static final String PASSENGER = "passenger";
    public static final String AIRLINE_ADMINISTRATOR = "airline_admin";

    public static final List<String> ROLES = List.of(SYSTEM_ADMIN, PASSENGER, AIRLINE_ADMINISTRATOR);
}
