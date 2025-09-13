package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
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
import org.acme.representation.user.AirlineAdministratorCreateRepresentation;

import java.util.Optional;

@ApplicationScoped
public class AirlineAdministratorService {
    @Inject
    AirlineAdministratorRepository airlineAdministratorRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    AddressMapper addressMapper;
    @Inject
    AirlineRepository airlineRepository;

    @Transactional
    public void createAirlineAdministrator(AirlineAdministratorCreateRepresentation airlineAdministratorCreateRepresentation){
        Optional<User> userOptional = userRepository.findUserByUsername(airlineAdministratorCreateRepresentation.getUsername());
        if(userOptional.isPresent()){
            throw new InvalidRequestException(ErrorMessages.USER_EXISTS);
        }

        Airline airline = getAirline(airlineAdministratorCreateRepresentation.getAirlineU2digitCode());

        AirlineAdministrator airlineAdministrator = new AirlineAdministrator(airlineAdministratorCreateRepresentation, airline);
        airlineAdministrator.getAddresses().addAll(airlineAdministratorCreateRepresentation.getAddresses().stream()
                .map(a -> {
                    Address address = addressMapper.mapToEntity(a);
                    address.setUser(airlineAdministrator);
                    return address;
                }).toList());
        airline.getAdministrators().add(airlineAdministrator);

        airlineAdministratorRepository.persist(airlineAdministrator);
    }

    private Airline getAirline(String u2digitCode){
        Optional<Airline> airlineOptional = airlineRepository.findOptionalAirlineByU2DigitCode(u2digitCode);
        if(airlineOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return airlineOptional.get();
    }
}
