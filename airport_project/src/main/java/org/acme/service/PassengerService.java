package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.Role;
import org.acme.domain.Address;
import org.acme.domain.Passenger;
import org.acme.domain.User;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AddressMapper;
import org.acme.persistence.PassengerRepository;
import org.acme.persistence.UserRepository;
import org.acme.representation.passenger.PassengerPassportRepresentation;
import org.acme.representation.passenger.PassengerUpdateRepresentation;
import org.acme.representation.user.PassengerCreateRepresentation;

import java.util.Optional;

@ApplicationScoped
@Transactional
public class PassengerService {

    @Inject
    PassengerRepository passengerRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    KeycloakService keycloakService;
    @Inject
    AddressMapper addressMapper;

    public PassengerPassportRepresentation getPassport(String username){
        Optional<Passenger> passengerOptional = passengerRepository.findPassengerByUsername(username);
        if(passengerOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        PassengerPassportRepresentation passengerPassportRepresentation = new PassengerPassportRepresentation();
        passengerPassportRepresentation.setPassport(passengerOptional.get().getPassport());
        return passengerPassportRepresentation;
    }

    public void createPassenger(PassengerCreateRepresentation passengerCreateRepresentation){
        Optional<User> userOptional = userRepository.findUserByUsername(passengerCreateRepresentation.getUsername());
        Optional<Passenger> passengerOptional = passengerRepository.findPassengerByPassport(passengerCreateRepresentation.getPassport());
        if(userOptional.isPresent() || passengerOptional.isPresent()){
            throw new InvalidRequestException(ErrorMessages.USER_EXISTS);
        }

        Passenger passenger = new Passenger(passengerCreateRepresentation);
        passenger.getAddresses().addAll(passengerCreateRepresentation.getAddresses().stream().map(addressCreateRepresentation ->
            {
                Address address = addressMapper.mapRepresentationToEntity(addressCreateRepresentation);
                address.setUser(passenger);
                return address;
            }
        ).toList());
        keycloakService.createKeycloakUser(passengerCreateRepresentation, Role.PASSENGER);
        passengerRepository.persist(passenger);
    }

    public void updatePassenger(PassengerUpdateRepresentation passengerUpdateRepresentation, String username) {
        Optional<User> userOptional = userRepository.findUserByUsername(username);
        if(userOptional.isEmpty() || (!userOptional.get().getRole().equals(Role.PASSENGER))){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        User user = userOptional.get();
        Optional<Passenger> passengerOptional = passengerRepository.findPassengerByPassport(passengerUpdateRepresentation.getPassport());
        if(!user.getUsername().equals(username) || (passengerOptional.isPresent() && !passengerOptional.get().getUsername().equals(username))){
            throw new InvalidRequestException(ErrorMessages.USER_EXISTS);
        }

        Passenger passenger = (Passenger) user;
        passenger.updateDetails(passengerUpdateRepresentation);
        passenger.setPassport(passengerUpdateRepresentation.getPassport());

        passenger.getAddresses().clear();
        passenger.getAddresses().addAll(passengerUpdateRepresentation.getAddresses().stream()
                .map(a -> {
                    Address address = addressMapper.mapRepresentationToEntity(a);
                    address.setUser(passenger);
                    return address;
                }).toList());
        passengerRepository.getEntityManager().merge(passenger);
    }
}
