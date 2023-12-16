package org.acme.persistence;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.domain.Reservation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class ReservationJPATest {

    @Inject
    EntityManager em;

    @Test
    @TestTransaction
    public void listReservations(){
        List<Reservation> result = em.createQuery("select r from Reservation r").getResultList();
        Assertions.assertEquals(2, result.size());
    }

}
