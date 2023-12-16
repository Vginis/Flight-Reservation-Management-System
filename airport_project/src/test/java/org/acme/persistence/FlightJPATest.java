package org.acme.persistence;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.acme.domain.Flight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class FlightJPATest {

    @Inject
    EntityManager em;

    @Test
    @TestTransaction
    public void listFlights(){
        List<Flight> result = em.createQuery("select p from Flight p").getResultList();
        Assertions.assertEquals(2, result.size());
    }

    @Test
    @TestTransaction
    public void listFlightsByAirlineCode(){
        Query query = em.createQuery("select f from Flight f where f.airline.u2digitCode=:airline");
        query.setParameter("airline", "A3");
        List<Flight> result = query.getResultList();
        Assertions.assertEquals(1, result.size());
    }

}
