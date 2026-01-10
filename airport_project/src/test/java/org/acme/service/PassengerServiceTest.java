package org.acme.service;

import jakarta.persistence.EntityManager;
import org.acme.constant.Role;
import org.acme.domain.Passenger;
import org.acme.domain.User;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.persistence.PassengerRepository;
import org.acme.persistence.UserRepository;
import org.acme.representation.passenger.PassengerUpdateRepresentation;
import org.acme.representation.user.PassengerCreateRepresentation;
import org.acme.util.UserContext;
import org.acme.util.UserUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

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
    @Mock
    UserContext userContext;
    @Mock
    EntityManager entityManager;

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
    void testGetPassport() {
        when(passengerRepository.findPassengerByUsername("username"))
                .thenReturn(Optional.of(passenger));

        Assertions.assertDoesNotThrow(() -> passengerService.getPassport("username"));
    }

    @Test
    void testGetPassportThrowsNotFoundException() {
        when(passengerRepository.findPassengerByUsername("username"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> passengerService.getPassport("username"));
    }

    @Test
    void createPassengerAsAdmin_shouldCreatePassengerAndKeycloakUser() {
        when(userRepository.findUserByUsername("username"))
                .thenReturn(Optional.empty());
        when(passengerRepository.findPassengerByPassport("passport123"))
                .thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() ->
                passengerService.createPassengerAsAdmin(passengerCreateRepresentation));

        verify(passengerRepository).persist(any(Passenger.class));
        verify(keycloakService).createKeycloakUser(passengerCreateRepresentation, Role.PASSENGER);
    }

    @Test
    void createPassengerAsAdmin_shouldThrow_whenUserExists() {
        when(userRepository.findUserByUsername("username"))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidRequestException.class,
                () -> passengerService.createPassengerAsAdmin(passengerCreateRepresentation));
    }

    @Test
    void completePassengerRegistration_shouldUseContextUsername() {
        when(userContext.extractUsername()).thenReturn("passenger1");
        when(userRepository.findUserByUsername("passenger1"))
                .thenReturn(Optional.empty());
        when(passengerRepository.findPassengerByPassport("passport123"))
                .thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() ->
                passengerService.completePassengerRegistration(passengerCreateRepresentation));

        verify(passengerRepository).persist(any(Passenger.class));
        verifyNoInteractions(keycloakService);
    }

    @Test
    void updatePassenger_shouldUpdatePassengerSuccessfully() {
        when(userRepository.findUserByUsername("username"))
                .thenReturn(Optional.of(passenger));
        passengerUpdateRepresentation.getAddresses().clear();
        when(passengerRepository.findPassengerByPassport("123"))
                .thenReturn(Optional.empty());
        when(passengerRepository.getEntityManager())
                .thenReturn(entityManager);

        Assertions.assertDoesNotThrow(() ->
                passengerService.updatePassenger(passengerUpdateRepresentation, "username"));
    }

    @Test
    void updatePassenger_shouldThrow_whenUserNotFound() {
        when(userRepository.findUserByUsername("missing"))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> passengerService.updatePassenger(passengerUpdateRepresentation, "missing"));
    }

    @Test
    void updatePassenger_shouldThrow_whenUserNotPassenger() {
        when(userRepository.findUserByUsername("admin"))
                .thenReturn(Optional.of(user));

        Assertions.assertThrows(InvalidRequestException.class,
                () -> passengerService.updatePassenger(passengerUpdateRepresentation, "admin"));
    }

    @Test
    void updatePassenger_shouldThrow_whenPassportBelongsToAnotherUser() {
        Passenger otherPassenger = new Passenger();
        when(userRepository.findUserByUsername("passenger1"))
                .thenReturn(Optional.of(passenger));
        when(passengerRepository.findPassengerByPassport("123"))
                .thenReturn(Optional.of(otherPassenger));

        Assertions.assertThrows(InvalidRequestException.class,
                () -> passengerService.updatePassenger(passengerUpdateRepresentation, "passenger1"));
    }
}
