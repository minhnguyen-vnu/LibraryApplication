package com.jmc.library.Assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RequestInfoTest {
    RequestInfo request;

    @BeforeEach
    void setUp() {
        request = new RequestInfo(
                1, 101, "Test Book", "testuser",
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2024, 12, 15),
                25.75,
                "Pending"
        );
    }

    @Test
    void testConstructor() {
        assertEquals(1, request.getIssueId());
        assertEquals(101, request.getBookId());
        assertEquals("Test Book", request.getBookName());
        assertEquals("testuser", request.getUsername());
        assertEquals(LocalDate.of(2024, 12, 1), request.getPickedDate());
        assertEquals(LocalDate.of(2024, 12, 15), request.getReturnDate());
        assertEquals(25.75, request.getTotalCost(), 0.01);
        assertEquals("Pending", request.getRequestStatus());
    }

    @Test
    void testSetAndGetIssueId() {
        request.setIssueId(2);
        assertEquals(2, request.getIssueId());
    }

    @Test
    void testSetAndGetBookId() {
        request.setBookId(202);
        assertEquals(202, request.getBookId());
    }

    @Test
    void testSetAndGetBookName() {
        request.setBookName("New Test Book");
        assertEquals("New Test Book", request.getBookName());
    }

    @Test
    void testSetAndGetUsername() {
        request.setUsername("newuser");
        assertEquals("newuser", request.getUsername());
    }

    @Test
    void testSetAndGetRequestStatus() {
        request.setRequestStatus("Approved");
        assertEquals("Approved", request.getRequestStatus());
    }

    @Test
    void testSetAndGetPickedDate() {
        LocalDate newPickedDate = LocalDate.of(2024, 12, 5);
        request.setPickedDate(newPickedDate);
        assertEquals(newPickedDate, request.getPickedDate());
    }

    @Test
    void testSetAndGetReturnDate() {
        LocalDate newReturnDate = LocalDate.of(2024, 12, 20);
        request.setReturnDate(newReturnDate);
        assertEquals(newReturnDate, request.getReturnDate());
    }

    @Test
    void testSetAndGetTotalCost() {
        request.setTotalCost(50.50);
        assertEquals(50.50, request.getTotalCost(), 0.01);
    }

    @Test
    void testToString() {
        String expected = "RequestInfo{issueId=1, bookId=101, username='testuser', " +
                "returnDate=2024-12-15, pickedDate=2024-12-01, totalCost=25.75, requestStatus='Pending'}";
        assertEquals(expected, request.toString());
    }
}
