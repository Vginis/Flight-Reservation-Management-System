package org.acme.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.acme.domain.City;
import org.acme.domain.Country;
import org.acme.persistence.CityRepository;
import org.acme.persistence.CountryRepository;
import org.apache.camel.Body;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CountryCityService {

    @Inject
    CountryRepository countryRepository;
    @Inject
    CityRepository cityRepository;

    @Transactional
    public void processCsvRow(List<String> rows) {

        String countryName = rows.getFirst();
        List<String> cityNames = rows.subList(1, rows.size());

        Optional<Country> countryOptional = countryRepository.findByName(countryName);
        if(countryOptional.isEmpty()){
            Country country = new Country(countryName);
            List<City> cities = getCities(cityNames, country);
            country.getCities().addAll(cities);
            countryRepository.persist(country);
        }
    }

    private List<City> getCities(List<String> cityNames, Country country) {
        List<City> cities = new ArrayList<>();
        for(String cityName: cityNames) {
            Optional<City> cityOptional = cityRepository.findByName(cityName);
            if(cityOptional.isPresent()) {
                continue;
            }

            cities.add(new City(cityName, country));
        }
        return cities;
    }
}
