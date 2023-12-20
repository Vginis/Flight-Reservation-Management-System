package org.acme.persistence;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import org.acme.domain.Ticket;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class TicketJPATest extends JPATest {

    @Test
    @Transactional
    public void listTickets(){
        List<Ticket> result = em.createQuery("select t from Ticket t").getResultList();
        assertEquals(3, result.size());
    }

}
