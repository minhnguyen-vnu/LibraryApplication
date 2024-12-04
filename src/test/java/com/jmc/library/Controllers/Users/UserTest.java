package com.jmc.library.Controllers.Users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

class UserTest {
    User user;

    @BeforeEach
    void setUp() {
        user = new User(
                "testuser",
                "password123",
                "Test User",
                LocalDate.of(1990, 1, 1),
                101,
                500.0,
                3,
                "active"
        );
    }

    @Test
    void testConstructor() {
        assertEquals("testuser", user.getUsername());
        assertEquals("password123", user.getPassword());
        assertEquals("Test User", user.getName());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthDate());
        assertEquals(101, user.getID());
        assertEquals(500.0, user.getTotalPaid(), 0.01);
        assertEquals(3, user.getTotalBorrowed());
        assertEquals("active", user.getStatus());
    }

    @Test
    void testSetAndGetUsername() {
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    void testSetAndGetPassword() {
        user.setPassword("newpassword");
        assertEquals("newpassword", user.getPassword());
    }

    @Test
    void testSetAndGetName() {
        user.setName("New Name");
        assertEquals("New Name", user.getName());
    }

    @Test
    void testSetAndGetBirthDate() {
        LocalDate newBirthDate = LocalDate.of(1995, 5, 10);
        user.setBirthDate(newBirthDate);
        assertEquals(newBirthDate, user.getBirthDate());
    }

    @Test
    void testSetAndGetID() {
        user.setID(202);
        assertEquals(202, user.getID());
    }

    @Test
    void testSetAndGetTotalPaid() {
        user.setTotalPaid(600.0);
        assertEquals(600.0, user.getTotalPaid(), 0.01);
    }

    @Test
    void testSetAndGetTotalBorrowed() {
        user.setTotalBorrowed(5);
        assertEquals(5, user.getTotalBorrowed());
    }

    @Test
    void testSetAndGetStatus() {
        user.setStatus("inactive");
        assertEquals("inactive", user.getStatus());
    }
}
