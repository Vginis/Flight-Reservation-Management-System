package org.acme.persistence;

import jakarta.persistence.Query;
import org.acme.domain.Flight;
import org.junit.jupiter.api.Test;

import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;

public class FlightJPATest extends JPATest{

    @Test
    public void listFlights(){
        List<Flight> result = em.createQuery("select p from Flight p").getResultList();
        assertEquals(2, result.size());
    }

    @Test
    public void listFlightsByAirlineCode(){
        Query query = em.createQuery("select f from Flight f where f.airline.u2digitCode=:airline");
        query.setParameter("airline", "A3");
        List<Flight> result = query.getResultList();
        assertEquals(1, result.size());
    }
}
