package org.acme.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.RollbackException;
import org.acme.domain.Airline;
import org.acme.domain.Airport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AirportJPATest extends JPATest {
    @Test
    public void listAirports() {
        List<Airport> result = em.createQuery("select a from Airport a").getResultList();
        assertEquals(2, result.size());
    }

    @Test
    public void denySavingAiportwithSameU3digitCode() {
        Airport ai1 = new Airport("Fotis Ioannidis","Athens","Greece","ATH");
        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(ai1);
            tx.commit();
        });
    }

    @Test
    public void denySavingAirportWithSameName() {
        Airport ai1 = new Airport("Eleftherios Venizelos","Athens","Greece","ATK");
        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(ai1);
            tx.commit();
        });
    }
}
