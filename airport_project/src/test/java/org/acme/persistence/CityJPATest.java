package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.domain.City;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class CityJPATest extends JPATest{

    @Inject
    CityRepository cityRepository;

    @Test
    void listCities() {
        List<?> result = em.createQuery("select c from City c").getResultList();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    void findByName_existingCity() {
        Optional<City> result = cityRepository.findByName("Athens");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Athens", result.get().getCityName());
    }

    @Test
    void findByName_nonExistingCountry() {
        Optional<City> result = cityRepository.findByName("Berlin");

        Assertions.assertTrue(result.isEmpty());
    }


    @Test
    void smartSearchCitiesCountryNull() {
        List<City> cities = cityRepository.smartSearchCities("Ath", null);
        Assertions.assertEquals(1, cities.size());
    }

    @Test
    void smartSearchCitiesCityNull() {
        List<City> cities = cityRepository.smartSearchCities("", "Spain");
        Assertions.assertEquals(1, cities.size());
    }
}
