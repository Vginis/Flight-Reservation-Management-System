package org.acme.persistence;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.acme.domain.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

@QuarkusTest
public class TicketJPATest {

    @Inject
    EntityManager em;

    @Test
    @TestTransaction
    public void listTickets(){
        List<Ticket> result = em.createQuery("select t from Ticket t").getResultList();
        Assertions.assertEquals(2, result.size());
    }

}
