package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.search.AircraftSortAndFilterBy;
import org.acme.domain.Aircraft;
import org.acme.domain.Airline;
import org.acme.domain.AirlineAdministrator;
import org.acme.domain.Flight;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AircraftMapper;
import org.acme.persistence.AircraftRepository;
import org.acme.persistence.AirlineAdministratorRepository;
import org.acme.representation.aircraft.AircraftCreateUpdateRepresentation;
import org.acme.representation.aircraft.AircraftRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;
import org.acme.util.UserContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AircraftService {

    @Inject
    AircraftRepository aircraftRepository;
    @Inject
    AirlineAdministratorRepository airlineAdministratorRepository;
    @Inject
    AircraftMapper aircraftMapper;
    @Inject
    UserContext userContext;

    public PageResult<AircraftRepresentation> searchAircraftsByParams(PageQuery<AircraftSortAndFilterBy> query){
        PageResult<Aircraft> aircraftPageResult = aircraftRepository.searchAircraftByParams(query);
        return aircraftMapper.map(aircraftPageResult);
    }

    @Transactional
    public void createAircraft(AircraftCreateUpdateRepresentation aircraftCreateRepresentation){
        Optional<AirlineAdministrator> airlineAdministratorOptional = airlineAdministratorRepository.findByUsername(userContext.extractUsername());
        if(airlineAdministratorOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        AirlineAdministrator airlineAdministrator = airlineAdministratorOptional.get();
        Airline airline = airlineAdministrator.getAirline();
        Aircraft aircraft = new Aircraft(aircraftCreateRepresentation, airline);
        airline.getAircrafts().add(aircraft);
        aircraftRepository.persist(aircraft);
    }

    @Transactional
    public void updateAircraft(AircraftCreateUpdateRepresentation aircraftUpdateRepresentation, Integer aircraftId){
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

        Aircraft aircraft = aircraftOptional.get();
        if(hasPendingFlight(aircraft)){
            throw new InvalidRequestException(ErrorMessages.AIRCRAFT_HAS_PENDING_FLIGHTS);
        }

        aircraftRepository.delete(aircraft);
    }

    private boolean hasPendingFlight(Aircraft aircraft){
        List<Flight> aircraftFlights = aircraft.getAirline().getFlights()
            .stream().filter(f -> f.getFlightSeatLayout().getAircraftId().equals(aircraft.getId())).toList();

        return aircraftFlights.stream().anyMatch(f -> f.getArrivalTime().isAfter(LocalDateTime.now()));
    }
}
