package org.acme.persistence;

import org.acme.domain.Airport;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AirportJPATest extends JPATest{
    @Test
    public void listAirports(){
        List<Airport> result = em.createQuery("select a from Airport a").getResultList();
        assertEquals(2, result.size());
    }
}
