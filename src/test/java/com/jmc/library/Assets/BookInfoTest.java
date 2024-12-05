package com.jmc.library.Assets;

import com.jmc.library.App;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BookInfoTest {

    private BookInfo bookInfo;

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        bookInfo = new BookInfo(1, "Test Book", "Test Author", 10, 100.0, LocalDate.of(2020, 1, 1), "1234567890");
    }

    @AfterEach
    void tearDown() {
        bookInfo = null;
    }

    @Test
    void testGetAndSetBookId() {
        bookInfo.setBookId(2);
        assertEquals(2, bookInfo.getBookId());
    }

    @Test
    void testGetAndSetBookName() {
        bookInfo.setBookName("New Book");
        assertEquals("New Book", bookInfo.getBookName());
    }

    @Test
    void testGetAndSetAuthorName() {
        bookInfo.setAuthorName("New Author");
        assertEquals("New Author", bookInfo.getAuthorName());
    }

    @Test
    void testGetAndSetQuantityInStock() {
        bookInfo.setQuantityInStock(20);
        assertEquals(20, bookInfo.getQuantityInStock());
    }

    @Test
    void testGetAndSetLeastPrice() {
        bookInfo.setLeastPrice(200.0);
        assertEquals(200.0, bookInfo.getLeastPrice());
    }

    @Test
    void testGetAndSetPublishedDate() {
        LocalDate newDate = LocalDate.of(2022, 2, 2);
        bookInfo.setPublishedDate(newDate);
        assertEquals(newDate, bookInfo.getPublishedDate());
    }

    @Test
    void testGetAndSetImageView() {
        ImageView newImageView = new ImageView();
        Image image = new Image(getClass().getResource("/IMAGES/avatar.png").toExternalForm());
        newImageView.setImage(image);
        bookInfo.setImageView(newImageView);
        assertEquals(newImageView, bookInfo.getImageView());
    }

    @Test
    void testImageViewProperty() {
        ImageView newImageView = new ImageView();
        Image image = new Image(getClass().getResource("/IMAGES/avatar.png").toExternalForm());
        newImageView.setImage(image);
        bookInfo.imageViewProperty().set(newImageView);
        assertEquals(newImageView, bookInfo.imageViewProperty().get());
    }

    @Test
    void testEquals() {
        BookInfo anotherBookInfo = new BookInfo(1, "Test Book", "Test Author", 10, 100.0, LocalDate.of(2020, 1, 1), "1234567890");
        assertEquals(bookInfo, anotherBookInfo);

        anotherBookInfo.setBookId(2);
        assertNotEquals(bookInfo, anotherBookInfo);
    }


    @Test
    void testToString() {
        String expected = "BookInfo{bookId=1, bookName='Test Book', authorName='Test Author', quantityInStock=IntegerProperty [value: 10], leastPrice=100.0, publishedDate=2020-01-01, ISBN='1234567890'}";
        assertEquals(expected, bookInfo.toString());
    }


}