package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.acme.domain.Flight;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class FlightJPATest extends JPATest {

    @Test
    @Transactional
    public void listFlights(){
        List<Flight> result = em.createQuery("select p from Flight p").getResultList();
        Assertions.assertEquals(4, result.size());
    }

    @Test
    @Transactional
    public void listFlightsByAirline(){
        Query query = em.createQuery("select f from Flight f where f.airline.id=:airline");
        query.setParameter("airline", 4);
        List<Flight> result = query.getResultList();
        Assertions.assertEquals(2, result.size());
    }

}
