package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.domain.Country;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class CountryJPATest extends JPATest {

    @Inject
    CountryRepository countryRepository;

    @Test
    void listCountries() {
        List<?> result = em.createQuery("select c from Country c").getResultList();
        Assertions.assertEquals(3, result.size());
    }

    @Test
    void findByName_existingCountry() {
        Optional<Country> result = countryRepository.findByName("Greece");

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals("Greece", result.get().getCountryName());
    }

    @Test
    void findByName_nonExistingCountry() {
        Optional<Country> result = countryRepository.findByName("Germany");

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void smartSearchCountries_withValue() {
        List<Country> result = countryRepository.smartSearchCountries("a");

        Assertions.assertEquals(2, result.size());

        List<String> names = result.stream()
                .map(Country::getCountryName)
                .toList();

        Assertions.assertTrue(names.contains("Italy"));
        Assertions.assertTrue(names.contains("Spain"));
    }

    @Test
    void smartSearchCountries_nullValue_returnsAll() {
        List<Country> result = countryRepository.smartSearchCountries(null);

        Assertions.assertEquals(3, result.size());
    }
}
