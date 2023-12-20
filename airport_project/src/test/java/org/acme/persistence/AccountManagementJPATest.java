package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.acme.domain.Administrator;
import org.acme.domain.Airline;
import org.acme.domain.Passenger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class AccountManagementJPATest extends JPATest {

    @Test
    @Transactional
    public void listPassengers() {
        List<Passenger> result = em.createQuery("select p from Passenger p").getResultList();
        Assertions.assertEquals(1, result.size());
    }

    @Test
    @Transactional
    public void listAirlines() {
        List<Airline> result = em.createQuery("select a from Airline a").getResultList();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @Transactional
    public void listAdministrator() {
        List<Administrator> result = em.createQuery("select ad from Administrator ad").getResultList();
        Assertions.assertEquals(1, result.size());
    }

    /*@Test
    @Transactional
    public void denySavingAirlineWithSamey2DigitCode() {
        Airline a3 = new Airline("Aegean Airlinessss", "A3", "aegn", "JeandDig1@");
        Assertions.assertThrows(RollbackException.class, () -> {
            em.detach(a3);
        });
    }

    @Test
    @Transactional
    public void denySavingAirlineWithSameName() {
        Airline a3 = new Airline("Aegean Airlines", "B3", "aegen", "JeandDig1@");
        Assertions.assertThrows(RollbackException.class, () -> {
            em.detach(a3);
        });
    }

    @Test
    @Transactional
    public void denySavingUserWithExistingUsername() {
        Airline a3 = new Airline("Aegean Airlines", "A3", "aegean", "JeandDig1@");
        Passenger p2 = new Passenger("e.gkinis@aueb.gr", "1234567891", "AK102545", "ndima", "JeandDig1@");

        Assertions.assertThrows(RollbackException.class, () -> {
            em.detach(a3);
        });

        Assertions.assertThrows(RollbackException.class, () -> {
            em.persist(p2);
        });
    }*/

}
