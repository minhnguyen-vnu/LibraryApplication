package com.jmc.library.Assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserBookInfoTest {
    UserBookInfo userBook;

    @BeforeEach
    void setUp() {
        userBook = new UserBookInfo(
                "Test Book",
                "Test Author",
                101,
                LocalDate.of(2024, 12, 1),
                LocalDate.of(2024, 12, 15),
                50.0,
                "Pending"
        );
    }

    @Test
    void testConstructor() {
        assertEquals("Test Book", userBook.getBookName());
        assertEquals("Test Author", userBook.getAuthorName());
        assertEquals(101, userBook.getBookId());
        assertEquals(LocalDate.of(2024, 12, 1), userBook.getPickedDate());
        assertEquals(LocalDate.of(2024, 12, 15), userBook.getReturnDate());
        assertEquals(50.0, userBook.getTotalCost(), 0.01);
        assertEquals("Pending", userBook.getRequestStatus());
        assertFalse(userBook.getRate());
        assertEquals(50.0, userBook.getSingleCost(), 0.01);
    }

    @Test
    void testSetAndGetBookName() {
        userBook.setBookName("New Book Name");
        assertEquals("New Book Name", userBook.getBookName());
    }

    @Test
    void testSetAndGetAuthorName() {
        userBook.setAuthorName("New Author");
        assertEquals("New Author", userBook.getAuthorName());
    }

    @Test
    void testSetAndGetBookId() {
        userBook.setBookId(202);
        assertEquals(202, userBook.getBookId());
    }

    @Test
    void testSetAndGetPickedDate() {
        LocalDate newPickedDate = LocalDate.of(2024, 12, 5);
        userBook.setPickedDate(newPickedDate);
        assertEquals(newPickedDate, userBook.getPickedDate());
    }

    @Test
    void testSetAndGetReturnDate() {
        LocalDate newReturnDate = LocalDate.of(2024, 12, 20);
        userBook.setReturnDate(newReturnDate);
        assertEquals(newReturnDate, userBook.getReturnDate());
    }

    @Test
    void testSetAndGetTotalCost() {
        userBook.setTotalCost(60.0);
        assertEquals(60.0, userBook.getTotalCost(), 0.01);
    }

    @Test
    void testSetAndGetRequestStatus() {
        userBook.setRequestStatus("Approved");
        assertEquals("Approved", userBook.getRequestStatus());
    }

    @Test
    void testSetAndGetIsRated() {
        userBook.setRate(false);
        assertFalse(userBook.getRate());
    }

    @Test
    void testSetAndGetSingleCost() {
        userBook.setSingleCost(30.0);
        assertEquals(30.0, userBook.getSingleCost(), 0.01);
    }
}
