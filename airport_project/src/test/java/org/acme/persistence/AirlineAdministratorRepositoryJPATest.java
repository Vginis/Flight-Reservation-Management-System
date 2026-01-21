package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.acme.domain.AirlineAdministrator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

@QuarkusTest
class AirlineAdministratorRepositoryJPATest extends JPATest {

    @Inject
    AirlineAdministratorRepository airlineAdministratorRepository;

    @Test
    void listAirlineAdministrators() {
        List<?> result = em.createQuery("select a from AirlineAdministrator a").getResultList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    void test_findByUsername() {
        Optional<AirlineAdministrator> airlineAdministrator = airlineAdministratorRepository.findByUsername("admin1");
        Assertions.assertTrue(airlineAdministrator.isPresent());
        Assertions.assertEquals("Alice", airlineAdministrator.get().getFirstName());
    }
}
