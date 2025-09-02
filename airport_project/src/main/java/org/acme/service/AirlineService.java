package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.domain.Airline;
import org.acme.exception.ResourceNotFoundException;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.AirlineCreateRepresentation;
import org.acme.mapper.AirlineMapper;
import org.acme.representation.AirlineRepresentation;
import org.acme.representation.AirlineUpdateRepresentation;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AirlineService {
    @Inject
    AirlineMapper airlineMapper;

    @Inject
    AirlineRepository airlineRepository;

    public List<AirlineRepresentation> searchAirlineByName(String name){
        return airlineMapper.toRepresentationList(airlineRepository.findAirlineByAirlineName(name));
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
        //Validate here if the airline already exists with annotation validator
        Airline airline = new Airline(airlineCreateRepresentation);
        airlineRepository.persist(airline);
    }

    @Transactional
    public void updateAirlineDetails(AirlineUpdateRepresentation airlineUpdateRepresentation){
        Optional<Airline> airlineToUpdate = airlineRepository.findByIdOptional(airlineUpdateRepresentation.getId());
        //Validate here if the airline already exists with annotation validator
        if(airlineToUpdate.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

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
