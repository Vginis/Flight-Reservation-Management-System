package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.Role;
import org.acme.domain.Address;
import org.acme.domain.Airline;
import org.acme.domain.AirlineAdministrator;
import org.acme.domain.AirlineLogo;
import org.acme.domain.User;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AddressMapper;
import org.acme.persistence.AirlineAdministratorRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.persistence.UserRepository;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.user.AirlineAdministratorCreateRepresentation;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.io.IOException;
import java.nio.file.Files;
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
    @Inject
    KeycloakService keycloakService;

    @Transactional
    public void createAirlineAdministrator(AirlineAdministratorCreateRepresentation airlineAdministratorCreateRepresentation,
                                           FileUpload airlineLogo) throws IOException {
        Optional<User> userOptional = userRepository.findUserByUsername(airlineAdministratorCreateRepresentation.getUsername());
        if(userOptional.isPresent()){
            throw new InvalidRequestException(ErrorMessages.USER_EXISTS);
        }

        Airline airline = getAirline(airlineAdministratorCreateRepresentation.getAirlineCreateRepresentation());

        AirlineAdministrator airlineAdministrator = new AirlineAdministrator(airlineAdministratorCreateRepresentation, airline);
        airlineAdministrator.getAddresses().addAll(airlineAdministratorCreateRepresentation.getAddresses().stream()
                .map(a -> {
                    Address address = addressMapper.mapToEntity(a);
                    address.setUser(airlineAdministrator);
                    return address;
                }).toList());
        airline.getAdministrators().add(airlineAdministrator);

        byte[] content = Files.readAllBytes(airlineLogo.uploadedFile());
        AirlineLogo logo = new AirlineLogo(airlineLogo.fileName(), airlineLogo.filePath().toString(), airlineLogo.contentType(),
                content, airline);
        airline.setLogo(logo);
        keycloakService.createKeycloakUser(airlineAdministratorCreateRepresentation, Role.AIRLINE_ADMINISTRATOR);
        airlineAdministratorRepository.persist(airlineAdministrator);
    }

    private Airline getAirline(AirlineCreateRepresentation airlineCreateRepresentation){
        Optional<Airline> airlineOptionalByCode = airlineRepository.findOptionalAirlineByU2DigitCode(airlineCreateRepresentation.getU2digitCode());
        Optional<Airline> airlineOptionalByName = airlineRepository.findOptionalAirlineByName(airlineCreateRepresentation.getAirlineName());
        if(airlineOptionalByCode.isEmpty() && airlineOptionalByName.isEmpty()){
            Airline airline = new Airline(airlineCreateRepresentation);
            airlineRepository.persist(airline);
            return airline;
        }

        if(airlineOptionalByCode.isEmpty()){
            throw new InvalidRequestException(ErrorMessages.AIRLINE_EXISTS);
        }

        return airlineOptionalByCode.get();
    }
}
