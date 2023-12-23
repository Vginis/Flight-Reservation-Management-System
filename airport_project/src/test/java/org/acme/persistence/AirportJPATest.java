package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.acme.domain.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class AirportJPATest extends JPATest {
    @Test
    @Transactional
    public void listAirports() {
        List<Airport> result = em.createQuery("select a from Airport a").getResultList();
        Assertions.assertEquals(4, result.size());
    }

    /*@Test
    @Transactional
    public void denySavingAirportWithSameU3digitCode() {
        Airport ai1 = new Airport("Fotis Ioannidis","Athens","Greece","ATH");
        Assertions.assertThrows(RollbackException.class, () -> {
            em.persist(ai1);
        });
    }

    @Test
    @Transactional
    public void denySavingAirportWithSameName() {
        Airport ai1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATK");
        Assertions.assertThrows(RollbackException.class, () -> {
            em.detach(ai1);
        });
    }*/

}
