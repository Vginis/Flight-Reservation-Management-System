package org.acme.service;

import org.acme.domain.City;
import org.acme.domain.Country;
import org.acme.persistence.CityRepository;
import org.acme.persistence.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryCityServiceTest {

    @InjectMocks
    CountryCityService countryCityService;
    @Mock
    CountryRepository countryRepository;
    @Mock
    CityRepository cityRepository;

    @Test
    void processCsvRow_shouldPersistCountryWithNewCities_whenCountryDoesNotExist() {
        List<String> row = List.of("Germany", "Berlin", "Munich");

        when(countryRepository.findByName("Germany")).thenReturn(Optional.empty());
        when(cityRepository.findByName("Berlin")).thenReturn(Optional.empty());
        when(cityRepository.findByName("Munich")).thenReturn(Optional.empty());

        countryCityService.processCsvRow(row);

        verify(countryRepository, times(1)).persist(any(Country.class));
        verify(cityRepository, times(1)).findByName("Berlin");
        verify(cityRepository, times(1)).findByName("Munich");
    }

    @Test
    void processCsvRow_shouldNotPersist_whenCountryAlreadyExists() {
        List<String> row = List.of("France", "Paris");
        Country existingCountry = new Country("France");

        when(countryRepository.findByName("France"))
                .thenReturn(Optional.of(existingCountry));
        countryCityService.processCsvRow(row);

        verify(countryRepository, never()).persist(any(Country.class));
        verifyNoInteractions(cityRepository);
    }

    @Test
    void processCsvRow_shouldSkipExistingCities() {
        List<String> row = List.of("Italy", "Rome", "Milan");

        when(countryRepository.findByName("Italy")).thenReturn(Optional.empty());
        when(cityRepository.findByName("Rome"))
                .thenReturn(Optional.of(new City("Rome", null)));
        when(cityRepository.findByName("Milan"))
                .thenReturn(Optional.empty());

        countryCityService.processCsvRow(row);

        verify(cityRepository).findByName("Rome");
        verify(cityRepository).findByName("Milan");
        verify(countryRepository).persist(any(Country.class));
    }

    @Test
    void smartSearchCountries_shouldReturnCountryNames() {
        when(countryRepository.smartSearchCountries("Ger"))
                .thenReturn(List.of(
                        new Country("Germany"),
                        new Country("Georgia")
                ));

        List<String> result = countryCityService.smartSearchCountries("Ger");

        assertEquals(List.of("Germany", "Georgia"), result);
    }

    @Test
    void smartSearchCities_shouldReturnCityNames() {
        when(cityRepository.smartSearchCities("Ber", "Germany"))
                .thenReturn(List.of(
                        new City("Berlin", null),
                        new City("Bergen", null)
                ));

        List<String> result = countryCityService.smartSearchCities("Ber", "Germany");

        assertEquals(List.of("Berlin", "Bergen"), result);
    }
}
