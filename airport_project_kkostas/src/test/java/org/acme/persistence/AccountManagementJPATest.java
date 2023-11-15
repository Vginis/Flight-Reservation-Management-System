package org.acme.persistence;

import org.acme.domain.AccountManagement;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountManagementJPATest extends JPATest{

    @SuppressWarnings("unchecked")
    @Test
    public void listAllAccounts() {
        List<AccountManagement> result = em.createQuery("select a from AccountManagement a").getResultList();
        assertEquals(3, result.size());
    }
}
