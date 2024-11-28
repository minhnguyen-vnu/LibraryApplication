package com.jmc.library.Account;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Admin();
    }

    @AfterEach
    void tearDown() {
        account = null;
    }

    @Test
    void getToken() {
        assertNull(account.getToken());

        account.setToken("testToken");
        assertEquals("testToken", account.getToken());
    }

    @Test
    void setToken() {
        account.setToken("newToken");
        assertEquals("newToken", account.getToken());
    }

    @Test
    void getUsername() {
        assertNull(account.getUsername());

        account.setUsername("testUser");
        assertEquals("testUser", account.getUsername());
    }

    @Test
    void setUsername() {
        account.setUsername("newUser");
        assertEquals("newUser", account.getUsername());
    }

    @Test
    void isAdmin() {
        assertTrue(account.isAdmin());
    }
}
