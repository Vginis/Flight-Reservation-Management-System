package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.Airline;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.AirlineCreateRepresentation;
import org.acme.representation.AirlineMapper;
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
            //TODO throw exception here
            return new AirlineRepresentation();
        }

        return airlineMapper.toRepresentation(airline.get());
    }

    public String getMostPopularAirport(Integer id){
        String stat = airlineRepository.getMostPopularAirportByAirline(id);
        //TODO Handle null value here differently
        return (stat!=null) ? stat : "";
    }

    public String getAirlineCompleteness(Integer id){
        Double stat = airlineRepository.getCompletenessByAirline(id);
        //TODO handle null values here differently
        return (stat!=null) ? stat.toString() : "";
    }

    @Transactional
    public void createAirline(AirlineCreateRepresentation airlineCreateRepresentation){
        //Validate here if the airline already exists: airlineRepository.checkIfCreatedAirlineExists();
        Airline airline = new Airline(airlineCreateRepresentation);
        airlineRepository.persist(airline);
    }

    @Transactional
    public void updateAirlineDetails(AirlineUpdateRepresentation airlineUpdateRepresentation){
        Optional<Airline> airlineToUpdate = airlineRepository.findByIdOptional(airlineUpdateRepresentation.getId());
        if(airlineToUpdate.isEmpty()){
            return;//ToDO handle Not foundException
        }

        airlineToUpdate.get().updateAirlineDetails(airlineUpdateRepresentation);
        airlineRepository.getEntityManager().merge(airlineToUpdate);
    }

    @Transactional
    public void deleteAirline(Integer id){
        Optional<Airline> airlineToDelete = airlineRepository.findByIdOptional(id);
        if(airlineToDelete.isEmpty()){
            return;//ToDO handle Not foundException
        }

        airlineRepository.delete(airlineToDelete.get());
    }
}
