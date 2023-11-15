package org.acme.persistence;

import org.acme.domain.Flight;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class FlightJPATest extends JPATest{

    @Test
    public void listFlights(){
        List<Flight> result = em.createQuery("select p from Flight p").getResultList();
        assertEquals(2, result.size());
    }
}
