package org.acme.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountManagementTest {

    private AccountManagement ac;

    @Test
    public void denyNotValidPassword() {
        assertThrows(RuntimeException.class, () -> ac = new AccountManagement("user", "123"));
        ac = new AccountManagement();
        assertThrows(RuntimeException.class, () -> ac.setPassword("abc"));
    }

}
