package com.jmc.library.Assets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GeneralBookInfoTest {
    GeneralBookInfo book;

    @BeforeEach
    void setUp() {
        book = new GeneralBookInfo();
    }

    @Test
    void testFullConstructor() {
        GeneralBookInfo book = new GeneralBookInfo(
                1, "Book Name", "Author Name", 10, 99.99,
                LocalDate.of(2023, 12, 1), "1234567890", "Publisher",
                "Genre", "English", "Description", "thumbnail.jpg"
        );

        assertEquals(1, book.getBookId());
        assertEquals("Book Name", book.getBookName());
        assertEquals("Author Name", book.getAuthorName());
        assertEquals(10, book.getQuantityInStock());
        assertEquals(99.99, book.getLeastPrice(), 0.01);
        assertEquals(LocalDate.of(2023, 12, 1), book.getPublishedDate());
        assertEquals("1234567890", book.getISBN());
        assertEquals("Publisher", book.getPublisher());
        assertEquals("Genre", book.getGenre());
        assertEquals("English", book.getOriginalLanguage());
        assertEquals("Description", book.getDescription());
        assertEquals("thumbnail.jpg", book.getThumbnail());
        assertTrue(book.isExist());
    }

    @Test
    void testSetAndGetBookId() {
        book.setBookId(123);
        assertEquals(123, book.getBookId());
    }

    @Test
    void testSetAndGetBookName() {
        book.setBookName("Test Book");
        assertEquals("Test Book", book.getBookName());
    }

    @Test
    void testSetAndGetAuthorName() {
        book.setAuthorName("Test Author");
        assertEquals("Test Author", book.getAuthorName());
    }

    @Test
    void testSetAndGetQuantityInStock() {
        book.setQuantityInStock(20);
        assertEquals(20, book.getQuantityInStock());
        assertEquals(20, book.quantityInStockProperty().get());
    }

    @Test
    void testSetAndGetLeastPrice() {
        book.setLeastPrice(99.99);
        assertEquals(99.99, book.getLeastPrice(), 0.01);
    }

    @Test
    void testSetAndGetPublishedDate() {
        LocalDate date = LocalDate.of(2024, 12, 1);
        book.setPublishedDate(date);
        assertEquals(date, book.getPublishedDate());
    }

    @Test
    void testSetAndGetISBN() {
        book.setISBN("1234567890");
        assertEquals("1234567890", book.getISBN());
    }

    @Test
    void testSetAndGetPublisher() {
        book.publisher = "Test Publisher"; // Publisher không có setter
        assertEquals("Test Publisher", book.getPublisher());
    }

    @Test
    void testSetAndGetGenre() {
        book.genre = "Fiction"; // Genre không có setter
        assertEquals("Fiction", book.getGenre());
    }

    @Test
    void testSetAndGetOriginalLanguage() {
        book.originalLanguage = "English"; // OriginalLanguage không có setter
        assertEquals("English", book.getOriginalLanguage());
    }

    @Test
    void testSetAndGetDescription() {
        book.description = "Test Description"; // Description không có setter
        assertEquals("Test Description", book.getDescription());
    }

    @Test
    void testSetAndGetThumbnail() {
        book.thumbnail = "test_thumbnail.jpg"; // Thumbnail không có setter
        assertEquals("test_thumbnail.jpg", book.getThumbnail());
    }

    @Test
    void testExistFlag() {
        book.exist = true; // Exist không có setter
        assertTrue(book.isExist());
    }
}
