package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.domain.Airline;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AirlineMapper;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.AirlineCreateRepresentation;
import org.acme.representation.AirlineRepresentation;
import org.acme.representation.AirlineUpdateRepresentation;

import java.util.Optional;

@ApplicationScoped
public class AirlineService {
    @Inject
    AirlineMapper airlineMapper;

    @Inject
    AirlineRepository airlineRepository;

    public AirlineRepresentation searchAirlineByName(String name){
        Optional<Airline> airline = airlineRepository.findOptionalAirlineByName(name);
        if(airline.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        return airlineMapper.toRepresentation(airline.get());
    }

    public AirlineRepresentation searchAirlineById(Integer id){
        Optional<Airline> airline = airlineRepository.findByIdOptional(id);
        if(airline.isEmpty()){
           throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        return airlineMapper.toRepresentation(airline.get());
    }

    public String getMostPopularAirport(Integer id){
        Optional<Airline> airline = airlineRepository.findByIdOptional(id);
        if(airline.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        return airline.get().mostPopularAirport();
    }

    public String getAirlineCompleteness(Integer id){
        Optional<Airline> airline = airlineRepository.findByIdOptional(id);
        if(airline.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }
        return airline.get().completeness().toString();
    }

    @Transactional
    public void createAirline(AirlineCreateRepresentation airlineCreateRepresentation){
        validateIfAirlineAlreadyExists(airlineCreateRepresentation);
        Airline airline = new Airline(airlineCreateRepresentation);
        airlineRepository.persist(airline);
    }

    private void validateIfAirlineAlreadyExists(AirlineCreateRepresentation airlineCreateRepresentation){
        Optional<Airline> airlineByName = airlineRepository.findOptionalAirlineByName(airlineCreateRepresentation.getAirlineName());
        Optional<Airline> airlineByCode = airlineRepository.findOptionalAirlineByU2DigitCode(airlineCreateRepresentation.getU2digitCode());
        if(airlineByName.isPresent() || airlineByCode.isPresent()){
            throw new InvalidRequestException(ErrorMessages.AIRLINE_EXISTS);
        }
    }

    @Transactional
    public void updateAirlineDetails(AirlineUpdateRepresentation airlineUpdateRepresentation){
        Optional<Airline> airlineToUpdate = airlineRepository.findByIdOptional(airlineUpdateRepresentation.getId());
        if(airlineToUpdate.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        validateIfAirlineAlreadyExists(airlineUpdateRepresentation);

        airlineToUpdate.get().updateAirlineDetails(airlineUpdateRepresentation);
        airlineRepository.getEntityManager().merge(airlineToUpdate);
    }

    @Transactional
    public void deleteAirline(Integer id){//Doesn't work. To be reviewed after the model layer restructuring
        Optional<Airline> airlineToDelete = airlineRepository.findByIdOptional(id);
        if(airlineToDelete.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        airlineRepository.delete(airlineToDelete.get());
    }
}
