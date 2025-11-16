package org.acme.service;

import org.acme.constant.Role;
import org.acme.domain.Address;
import org.acme.domain.Airline;
import org.acme.domain.AirlineAdministrator;
import org.acme.domain.User;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AddressMapper;
import org.acme.persistence.AirlineAdministratorRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.UserRepository;
import org.acme.representation.AddressCreateRepresentation;
import org.acme.representation.user.AirlineAdministratorCreateRepresentation;
import org.acme.util.AddressUtil;
import org.acme.util.AirlineUtil;
import org.acme.util.UserUtil;
import org.jboss.resteasy.reactive.multipart.FileUpload;
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
class AirlineAdministratorServiceTest {
    @InjectMocks
    AirlineAdministratorService airlineAdministratorService;
    @Mock
    UserRepository userRepository;
    @Mock
    AirlineRepository airlineRepository;
    @Mock
    AddressMapper addressMapper;
    @Mock
    AirlineAdministratorRepository airlineAdministratorRepository;
    @Mock
    KeycloakService keycloakService;
    @Mock
    FileUpload fileUpload;

    User user;
    Airline airline;
    AirlineAdministratorCreateRepresentation airlineAdministratorCreateRepresentation;
    Address address;
    @BeforeEach
    void setup(){
        airline = AirlineUtil.createAirline();
        airlineAdministratorCreateRepresentation = UserUtil.createAirlineAdministratorCreateRepresentation();
        address = AddressUtil.createAddress();
    }

    @Test
    void testCreateAirlineAdministrator(){
        Mockito.when(userRepository.findUserByUsername("user-1"))
                .thenReturn(Optional.empty());
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.of(airline));

//        Mockito.when(addressMapper.ma(Mockito.any(AddressCreateRepresentation.class)))
//                .thenReturn(address);
        Mockito.doNothing().when(airlineAdministratorRepository)
                .persist(Mockito.any(AirlineAdministrator.class));
        Mockito.doNothing().when(keycloakService)
                .createKeycloakUser(airlineAdministratorCreateRepresentation, Role.AIRLINE_ADMINISTRATOR);

        Assertions.assertDoesNotThrow(() -> airlineAdministratorService
                .createAirlineAdministrator(airlineAdministratorCreateRepresentation, fileUpload));
    }

    @Test
    void testCreateAirlineAdministratorThrowsInvalidRequestException(){
        Mockito.when(userRepository.findUserByUsername("user-1"))
                .thenReturn(Optional.of(new User()));
        Assertions.assertThrows(InvalidRequestException.class,
                () -> airlineAdministratorService.
                        createAirlineAdministrator(airlineAdministratorCreateRepresentation, fileUpload));
    }

    @Test
    void testCreateAirlineAdministratorThrowsResourceNotFoundException(){
        Mockito.when(userRepository.findUserByUsername("user-1"))
                .thenReturn(Optional.empty());
        Mockito.when(airlineRepository.findOptionalAirlineByU2DigitCode("AA"))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> airlineAdministratorService.
                        createAirlineAdministrator(airlineAdministratorCreateRepresentation, fileUpload));
    }
}
