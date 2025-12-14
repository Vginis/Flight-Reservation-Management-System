package org.acme.service;

import org.acme.constant.Role;
import org.acme.domain.Passenger;
import org.acme.domain.User;
import org.acme.exception.InvalidRequestException;
import org.acme.persistence.PassengerRepository;
import org.acme.persistence.UserRepository;
import org.acme.representation.passenger.PassengerUpdateRepresentation;
import org.acme.representation.user.PassengerCreateRepresentation;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    @InjectMocks
    PassengerService passengerService;
    @Mock
    PassengerRepository passengerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    KeycloakService keycloakService;

    PassengerCreateRepresentation passengerCreateRepresentation;
    User user;
    Passenger passenger;
    PassengerUpdateRepresentation passengerUpdateRepresentation;
    @BeforeEach
    void setup(){
        passengerCreateRepresentation = UserUtil.createPassengerCreateRepresentation();
        user = UserUtil.createUser();
        passenger = new Passenger(passengerCreateRepresentation);
        passengerUpdateRepresentation = UserUtil.createPassengerUpdateRepresentation();
    }

    @Test
    void test_create_passenger(){
        Mockito.when(userRepository.findUserByUsername("username")).thenReturn(Optional.empty());
        Mockito.when(passengerRepository.findPassengerByPassport("passport123")).thenReturn(Optional.empty());
        Mockito.doNothing().when(keycloakService).createKeycloakUser(passengerCreateRepresentation, Role.PASSENGER);
        Mockito.doNothing().when(passengerRepository).persist(Mockito.any(Passenger.class));
        Assertions.assertDoesNotThrow(() -> passengerService.createPassengerAsAdmin(passengerCreateRepresentation));
    }

    @Test
    void test_create_passenger_user_exists(){
        Mockito.when(userRepository.findUserByUsername("username")).thenReturn(Optional.empty());
        Mockito.when(passengerRepository.findPassengerByPassport("passport123")).thenReturn(Optional.of(new Passenger()));
        Assertions.assertThrows(InvalidRequestException.class, () -> passengerService.createPassengerAsAdmin(passengerCreateRepresentation));
    }

    @Test
    void test_update_passenger(){
        Mockito.when(userRepository.findUserByUsername("username")).thenReturn(Optional.of(passenger));

        
    }
}
