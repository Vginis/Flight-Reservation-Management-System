package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.domain.Aircraft;
import org.acme.domain.Airline;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AircraftMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.aircraft.AircraftCreateRepresentation;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.acme.representation.aircraft.AircraftUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;

import java.util.Optional;

@ApplicationScoped
public class AircraftService {

    @Inject
    AircraftRepository aircraftRepository;
    @Inject
    AirlineRepository airlineRepository;
    @Inject
    AircraftMapper aircraftMapper;

    public PageResult<AircraftRepresentation> searchAircraftsByParams(PageQuery<AircraftSortAndFilterBy> query){
        PageResult<Aircraft> aircraftPageResult = aircraftRepository.searchAircraftByParams(query);
        return aircraftMapper.map(aircraftPageResult);
    }

    @Transactional
    public void createAircraft(AircraftCreateRepresentation aircraftCreateRepresentation){
        Optional<Airline> airlineOptional = airlineRepository.findByIdOptional(aircraftCreateRepresentation.getAirlineId());
        if(airlineOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Airline airline = airlineOptional.get();
        Aircraft aircraft = new Aircraft(aircraftCreateRepresentation, airline);
        airline.getAircrafts().add(aircraft);
        aircraftRepository.persist(aircraft);
    }

    @Transactional
    public void updateAircraft(AircraftUpdateRepresentation aircraftUpdateRepresentation, Integer aircraftId){
        Optional<Aircraft> aircraftOptional = aircraftRepository.findByIdOptional(aircraftId);
        if(aircraftOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Aircraft aircraft = aircraftOptional.get();
        aircraft.edit(aircraftUpdateRepresentation);
        aircraftRepository.getEntityManager().merge(aircraft);
    }

    @Transactional
    public void deleteAircraft(Integer aircraftId){
        Optional<Aircraft> aircraftOptional = aircraftRepository.findByIdOptional(aircraftId);
        if(aircraftOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        aircraftRepository.delete(aircraftOptional.get());
    }
}
