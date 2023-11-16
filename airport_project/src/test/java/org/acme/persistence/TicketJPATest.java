package org.acme.persistence;

import org.acme.domain.Ticket;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TicketJPATest extends JPATest {

    @Test
    public void listTickets(){
        List<Ticket> result = em.createQuery("select t from Ticket t").getResultList();
        assertEquals(1, result.size());
    }

}
