package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.domain.Airport;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AirportMapper;
import org.acme.persistence.AirportRepository;
import org.acme.representation.AirportCreateRepresentation;
import org.acme.representation.AirportRepresentation;

import java.util.Optional;

@ApplicationScoped
public class AirportService {
    @Inject
    AirportRepository airportRepository;

    @Inject
    AirportMapper airportMapper;

    public AirportRepresentation findAirportBy3DCode(String code) {
        Optional<Airport> airportOptional = airportRepository.findAirportBy3DCode(code);
        if(airportOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        return airportMapper.toRepresentation(airportOptional.get());
    }

    public AirportRepresentation findAirportById(Integer airportId){
        Optional<Airport> airportOptional = airportRepository.findByIdOptional(airportId);
        if(airportOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return airportMapper.toRepresentation(airportOptional.get());
    }

    @Transactional
    public void createAirport(AirportCreateRepresentation airportCreateRepresentation){
        Optional<Airport> airportOptionalBy3DCode = airportRepository.findAirportBy3DCode(airportCreateRepresentation.getU3digitCode());
        Optional<Airport> airportOptionalByName = airportRepository.findAirportByName(airportCreateRepresentation.getAirportName());
        if(airportOptionalBy3DCode.isPresent() || airportOptionalByName.isPresent()){
            throw new InvalidRequestException(ErrorMessages.AIRPORT_EXISTS);
        }

        Airport createdAirport = new Airport(airportCreateRepresentation.getAirportName(), airportCreateRepresentation.getCity(),
                airportCreateRepresentation.getCountry(), airportCreateRepresentation.getU3digitCode());
        airportRepository.persist(createdAirport);
    }

    @Transactional
    public void updateAirport(AirportCreateRepresentation airportCreateRepresentation){
        Optional<Airport> airportOptional = airportRepository.findAirportBy3DCode(airportCreateRepresentation.getU3digitCode());
        if(airportOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Airport airport = airportOptional.get();
        airport.update(airportCreateRepresentation);
        airportRepository.getEntityManager().merge(airport);
    }

    @Transactional
    public void deleteAirport(Integer id){
        Optional<Airport> airportOptional = airportRepository.findByIdOptional(id);
        if(airportOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        airportRepository.delete(airportOptional.get());
    }
}
