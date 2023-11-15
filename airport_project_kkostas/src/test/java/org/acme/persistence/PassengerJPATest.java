package org.acme.persistence;

import org.acme.domain.AccountManagement;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class PassengerJPATest extends JPATest {

    @SuppressWarnings("unchecked")
    @Test
    public void listPassengers(){
        List<AccountManagement> result = em.createQuery("select p from AccountManagement p").getResultList();
        assertEquals(3, result.size());
    }


}
