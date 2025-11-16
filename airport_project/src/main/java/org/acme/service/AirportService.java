package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.constant.ErrorMessages;
import org.acme.constant.search.AirportSortAndFilterBy;
import org.acme.domain.Airport;
import org.acme.domain.City;
import org.acme.domain.Country;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.mapper.AirportMapper;
import org.acme.persistence.AirportRepository;
import org.acme.persistence.CityRepository;
import org.acme.persistence.CountryRepository;
import org.acme.representation.airport.AirportCreateRepresentation;
import org.acme.representation.airport.AirportRepresentation;
import org.acme.representation.airport.AirportUpdateRepresentation;
import org.acme.search.PageQuery;
import org.acme.search.PageResult;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class AirportService {

    @Inject
    AirportRepository airportRepository;
    @Inject
    AirportMapper airportMapper;
    @Inject
    CountryRepository countryRepository;
    @Inject
    CityRepository cityRepository;

    public PageResult<AirportRepresentation> searchAirportsByParams(PageQuery<AirportSortAndFilterBy> query){
        return airportMapper.map(airportRepository.searchAirportsByParams(query));
    }

    public List<AirportRepresentation> smartSearchAirports(String value){
        return airportMapper.map(airportRepository.smartSearchAirports(value));
    }

    @Transactional
    public void createAirport(AirportCreateRepresentation airportCreateRepresentation){
        validateIfAirportExists(airportCreateRepresentation);
        Optional<Country> countryOptional = countryRepository.findByName(airportCreateRepresentation.getCountry());
        Optional<City> cityOptional = cityRepository.findByName(airportCreateRepresentation.getCity());
        if(countryOptional.isEmpty() || cityOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Country country = countryOptional.get();
        City city = cityOptional.get();
        if(!country.getCities().stream().map(City::getCityName).toList().contains(city.getCityName())) {
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }

        Airport createdAirport = new Airport(airportCreateRepresentation.getAirportName(), city,
                country, airportCreateRepresentation.getU3digitCode());
        airportRepository.persist(createdAirport);
    }

    private void validateIfAirportExists(AirportCreateRepresentation airportCreateRepresentation){
        Optional<Airport> airportOptionalBy3DCode = airportRepository.findAirportBy3DCode(airportCreateRepresentation.getU3digitCode());
        Optional<Airport> airportOptionalByName = airportRepository.findAirportByName(airportCreateRepresentation.getAirportName());
        if(airportOptionalBy3DCode.isPresent() || airportOptionalByName.isPresent()){
            throw new InvalidRequestException(ErrorMessages.AIRPORT_EXISTS);
        }
    }

    private void validateIfAirportExistsForUpdate(AirportUpdateRepresentation airportUpdateRepresentation){
        Optional<Airport> airportOptionalBy3DCode = airportRepository.findAirportBy3DCode(airportUpdateRepresentation.getU3digitCode());
        Optional<Airport> airportOptionalByName = airportRepository.findAirportByName(airportUpdateRepresentation.getAirportName());
        if((airportOptionalBy3DCode.isPresent() && !airportOptionalBy3DCode.get().getId().equals(airportUpdateRepresentation.getId())) ||
                (airportOptionalByName.isPresent() && !airportOptionalByName.get().getId().equals(airportUpdateRepresentation.getId()))){
            throw new InvalidRequestException(ErrorMessages.AIRPORT_EXISTS);
        }
    }

    @Transactional
    public void updateAirport(AirportUpdateRepresentation airportUpdateRepresentation){
        Optional<Airport> airportOptional = airportRepository.findByIdOptional(airportUpdateRepresentation.getId());
        if(airportOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        validateIfAirportExistsForUpdate(airportUpdateRepresentation);
        Airport airport = airportOptional.get();

        Optional<Country> countryOptional = countryRepository.findByName(airportUpdateRepresentation.getCountry());
        Optional<City> cityOptional = cityRepository.findByName(airportUpdateRepresentation.getCity());
        if(countryOptional.isEmpty() || cityOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Country country = countryOptional.get();
        City city = cityOptional.get();
        if(!country.getCities().stream().map(City::getCityName).toList().contains(city.getCityName())) {
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }

        airport.update(airportUpdateRepresentation.getAirportName(), city, country);
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
