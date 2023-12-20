package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.acme.domain.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class ReservationJPATest extends JPATest {

    @Test
    @Transactional
    public void listReservations(){
        List<Reservation> result = em.createQuery("select r from Reservation r").getResultList();
        Assertions.assertEquals(2, result.size());
    }

}
