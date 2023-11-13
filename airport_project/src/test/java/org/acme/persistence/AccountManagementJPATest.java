package org.acme.persistence;

import org.acme.domain.AccountManagement;
import org.acme.domain.Airline;
import org.acme.domain.Passenger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AccountManagementJPATest extends JPATest {

    @Test
    public void listPassengers(){
        List<Passenger> result = em.createQuery("select p from Passenger p").getResultList();
        assertEquals(1, result.size());
    }

    @Test
    public void listAirlines(){
        List<Airline> result = em.createQuery("select a from Airline a").getResultList();
        assertEquals(1, result.size());
    }

}
