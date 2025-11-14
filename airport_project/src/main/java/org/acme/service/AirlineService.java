package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.search.AirlineSortAndFilterBy;
import org.acme.domain.Airline;
import org.acme.domain.AirlineLogo;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AirlineMapper;
import org.acme.persistence.AirlineRepository;
import org.acme.representation.airline.AirlineCreateRepresentation;
import org.acme.representation.airline.AirlineRepresentation;
import org.acme.representation.airline.AirlineUpdateRepresentation;
import org.acme.representation.file.AirlineFileRepresentation;
import org.acme.representation.file.FileRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class AirlineService {

    @Inject
    AirlineMapper airlineMapper;

    @Inject
    AirlineRepository airlineRepository;

    public PageResult<AirlineRepresentation> searchAirlinesByParams(PageQuery<AirlineSortAndFilterBy> query){
        List<AirlineRepresentation> airlineRepresentations = new ArrayList<>();
        PageResult<Airline> airlinePageResult = airlineRepository.searchAirlinesByParams(query);
        for(Airline airline: airlinePageResult.getResults()){
            AirlineRepresentation airlineRepresentation = airlineMapper.map(airline);
            AirlineLogo logo = airline.getLogo();
            airlineRepresentation.setFileRepresentation(new FileRepresentation(logo.getFileName(), logo.getContentType(),
                    logo.getContent()));

            airlineRepresentations.add(airlineRepresentation);
        }
        return new PageResult<>(airlinePageResult.getTotal(), airlineRepresentations);
    }

    public List<AirlineFileRepresentation> getAirlineLogos(List<String> airlineCodes) {
        List<AirlineFileRepresentation> fileRepresentations = new ArrayList<>();
        for(String code: airlineCodes) {
            Optional<Airline> airlineOptional = airlineRepository.findOptionalAirlineByU2DigitCode(code);
            if(airlineOptional.isEmpty()){
                throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
            }

            Airline airline = airlineOptional.get();
            AirlineLogo logo = airline.getLogo();
            fileRepresentations.add(new AirlineFileRepresentation(logo.getFileName(), logo.getContentType(),
                    logo.getContent(),airline.getU2digitCode()));
        }

        return fileRepresentations;
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

    private void validateIfAirlineAlreadyExistsForUpdate(AirlineUpdateRepresentation airlineUpdateRepresentation){
        Optional<Airline> airlineByName = airlineRepository.findOptionalAirlineByName(airlineUpdateRepresentation.getAirlineName());
        Optional<Airline> airlineByCode = airlineRepository.findOptionalAirlineByU2DigitCode(airlineUpdateRepresentation.getU2digitCode());
        if((airlineByName.isPresent() && checkIfIsSameAirline(airlineByName.get(), airlineUpdateRepresentation))
                || (airlineByCode.isPresent() && checkIfIsSameAirline(airlineByCode.get(), airlineUpdateRepresentation))){
            throw new InvalidRequestException(ErrorMessages.AIRLINE_EXISTS);
        }
    }

    private boolean checkIfIsSameAirline(Airline airline, AirlineUpdateRepresentation airlineUpdateRepresentation){
        return !Objects.equals(airline.getId(), airlineUpdateRepresentation.getId());
    }

    @Transactional
    public void updateAirlineDetails(AirlineUpdateRepresentation airlineUpdateRepresentation){
        Optional<Airline> airlineToUpdate = airlineRepository.findByIdOptional(airlineUpdateRepresentation.getId());
        if(airlineToUpdate.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        validateIfAirlineAlreadyExistsForUpdate(airlineUpdateRepresentation);
        Airline airline = airlineToUpdate.get();
        airline.updateAirlineDetails(airlineUpdateRepresentation);
        airlineRepository.getEntityManager().merge(airline);
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
