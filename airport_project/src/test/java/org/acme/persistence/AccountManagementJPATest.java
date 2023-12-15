package org.acme.persistence;

import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.RollbackException;
import org.acme.domain.Administrator;
import org.acme.domain.Airline;
import org.acme.domain.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountManagementJPATest extends JPATest {

    @Test
    public void listPassengers() {
        List<Passenger> result = em.createQuery("select p from Passenger p").getResultList();
        assertEquals(1, result.size());
    }

    @Test
    public void listAirlines() {
        List<Airline> result = em.createQuery("select a from Airline a").getResultList();
        assertEquals(2, result.size());
    }


    @Test
    public void listAdministrator() {
        List<Administrator> result = em.createQuery("select ad from Administrator ad").getResultList();
        assertEquals(1, result.size());
    }

    @Test
    public void denySavingAirlineWithSamey2DigitCode() {
        Airline a3 = new Airline("Aegean Airlinessss", "A3", "aegn", "JeandDig1@");
        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(a3);
            tx.commit();
        });
    }

    @Test
    public void denySavingAirlineWithSameName() {
        Airline a3 = new Airline("Aegean Airlines", "B3", "aegen", "JeandDig1@");
        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(a3);
            tx.commit();
        });
    }

    @Test
    public void denySavingUserWithExistingUsername() {
        Airline a3 = new Airline("Aegean Airlines", "A3", "aegean", "JeandDig1@");
        Passenger p2 = new Passenger("e.gkinis@aueb.gr", "1234567891", "AK102545", "ndima", "JeandDig1@");

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(a3);
            tx.commit();
        });

        Assertions.assertThrows(RollbackException.class, () -> {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(p2);
            tx.commit();
        });
    }

}
