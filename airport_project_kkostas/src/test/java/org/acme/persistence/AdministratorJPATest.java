package org.acme.persistence;

import org.acme.domain.AccountManagement;
import org.junit.jupiter.api.Test;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdministratorJPATest extends JPATest {

    @SuppressWarnings("unchecked")
    @Test
    public void listAdministrator(){
        List<AccountManagement> result = em.createQuery("select d from AccountManagement d").getResultList();
        assertEquals(3, result.size());
    }


}