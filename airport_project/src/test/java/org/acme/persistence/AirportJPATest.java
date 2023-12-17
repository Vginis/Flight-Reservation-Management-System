package org.acme.persistence;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.domain.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class AirportJPATest {

    @Inject
    EntityManager em;

    @Test
    @TestTransaction
    public void listAirports() {
        List<Airport> result = em.createQuery("select a from Airport a").getResultList();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @TestTransaction
    public void denySavingAiportwithSameU3digitCode() {
        Airport ai1 = new Airport("Fotis Ioannidis","Athens","Greece","ATH");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            em.persist(ai1);
        });
    }

    @Test
    @TestTransaction
    public void denySavingAirportWithSameName() {
        Airport ai1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATK");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            em.persist(ai1);
        });
    }

}
