package org.acme.persistence;

import org.acme.domain.Passenger;
import org.acme.domain.Reservation;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReservationJPATest extends JPATest {

    @Test
    public void listReservations(){
        List<Reservation> result = em.createQuery("select r from Reservation r").getResultList();
        assertEquals(2, result.size());
    }

}
