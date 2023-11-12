package org.acme.persistence;

import org.acme.domain.AccountManagement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PassengerJPATest extends JPATest {

    @Test
    public void listPassengers(){
        List<AccountManagement> result = em.createQuery("select p from AccountManagement p").getResultList();
        assertEquals(1, result.size());
    }

}
