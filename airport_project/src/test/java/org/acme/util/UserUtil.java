package org.acme.util;

import org.acme.constant.Role;
import org.acme.domain.Airline;
import org.acme.domain.AirlineAdministrator;
import org.acme.domain.User;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.passenger.PassengerUpdateRepresentation;
import org.acme.representation.user.*;

import java.util.ArrayList;
import java.util.List;

public class UserUtil {

    public static AirlineAdministratorCreateRepresentation createAirlineAdministratorCreateRepresentation(){
        return new AirlineAdministratorCreateRepresentation("user-1","Kendrick", "Nunn","email@email.com","1234557890",
                new ArrayList<>(List.of(AddressUtil.createAddressCreateRepresentation())),AirlineUtil.createAirlineCreateRepresentation());
    }

    public static AirlineAdministrator createAirlineAdministrator(Airline airline) {
        return new AirlineAdministrator(createUserCreateRepresentation(), airline);
    }

    public static UserRepresentation createUserRepresentation(){
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(1);
        userRepresentation.setUsername("user-1");
        userRepresentation.setEmail("email@email.com");
        userRepresentation.setFirstName("john");
        userRepresentation.setLastName("doe");
        userRepresentation.setRole(Role.PASSENGER);
        userRepresentation.setAddresses(new ArrayList<>());
        return userRepresentation;
    }

    public static UserCreateRepresentation createUserCreateRepresentation(){
        return new UserCreateRepresentation("user-1","john","doe","email@email.com",
                "1234567890",new ArrayList<>());
    }

    public static User createUser(){
        return new User(createUserCreateRepresentation(), Role.PASSENGER);
    }

    public static UserUpdateRepresentation createUserUpdateRepresentation(){
        UserUpdateRepresentation userUpdateRepresentation = new UserUpdateRepresentation();
        userUpdateRepresentation.setFirstName("firstName");
        userUpdateRepresentation.setLastName("lastName");
        userUpdateRepresentation.setEmail("email@email.com");
        userUpdateRepresentation.setPhoneNumber("1234567899");
        userUpdateRepresentation.setAddresses(new ArrayList<>());
        userUpdateRepresentation.getAddresses().add(AddressUtil.createAddressCreateRepresentation());
        return userUpdateRepresentation;
    }

    public static PassengerCreateRepresentation createPassengerCreateRepresentation(){
        return new PassengerCreateRepresentation("username","firstName","lastName","email@email.com",
                "1234567890",new ArrayList<>(),"passport123");
    }

    public static PassengerUpdateRepresentation createPassengerUpdateRepresentation(){
        PassengerUpdateRepresentation passengerUpdateRepresentation = (PassengerUpdateRepresentation) createUserUpdateRepresentation();
        passengerUpdateRepresentation.setPassport("123");
        return passengerUpdateRepresentation;
    }
}
