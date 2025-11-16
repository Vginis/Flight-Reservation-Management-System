package org.acme.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.constant.ErrorMessages;
import org.acme.domain.Address;
import org.acme.domain.City;
import org.acme.domain.Country;
import org.acme.exception.InvalidRequestException;
import org.acme.exception.ResourceNotFoundException;
import org.acme.persistence.CityRepository;
import org.acme.persistence.CountryRepository;
import org.acme.representation.AddressCreateRepresentation;

import java.util.Optional;

@ApplicationScoped
public class AddressMapper {
    @Inject
    CountryRepository countryRepository;
    @Inject
    CityRepository cityRepository;

    AddressMapper(){
        //Intentionally left blank
    }

    public Address mapRepresentationToEntity(AddressCreateRepresentation addressCreateRepresentation) {
        Optional<Country> countryOptional = countryRepository.findByName(addressCreateRepresentation.getCountry());
        Optional<City> cityOptional = cityRepository.findByName(addressCreateRepresentation.getCity());
        if(countryOptional.isEmpty() || cityOptional.isEmpty()){
            throw new ResourceNotFoundException(ErrorMessages.ENTITY_NOT_FOUND);
        }

        Country country = countryOptional.get();
        City city = cityOptional.get();
        if(!country.getCities().stream().map(City::getCityName).toList().contains(city.getCityName())) {
            throw new InvalidRequestException(ErrorMessages.INVALID_VALUE);
        }

        return new Address(addressCreateRepresentation.getAddressName(), city, country);
    }
}
