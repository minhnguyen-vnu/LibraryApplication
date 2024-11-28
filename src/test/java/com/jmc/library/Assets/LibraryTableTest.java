//package com.jmc.library.Assets;

package com.jmc.library.Assets;

import com.jmc.library.Database.DBQuery;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LibraryTableMockitoTest {

    private LibraryTable libraryTable;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        libraryTable = new LibraryTable();
        libraryTable.store_tb = new TableView<>();
        libraryTable.bookList = FXCollections.observableArrayList();
    }

    @Test
    void testShowLibraryWithMockResultSet() throws SQLException {
        // Mock ResultSet
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configure mock ResultSet
        when(mockResultSet.next()).thenReturn(true).thenReturn(false); // Chỉ trả về 1 dòng
        when(mockResultSet.getInt("bookId")).thenReturn(1);
        when(mockResultSet.getString("bookName")).thenReturn("Mock Book");
        when(mockResultSet.getString("authorName")).thenReturn("Mock Author");
        when(mockResultSet.getInt("quantityInStock")).thenReturn(10);
        when(mockResultSet.getDouble("leastPrice")).thenReturn(19.99);
        when(mockResultSet.getDate("publishDate")).thenReturn(Date.valueOf(LocalDate.of(2023, 1, 1)));
        when(mockResultSet.getString("ISBN")).thenReturn("1234567890");
        when(mockResultSet.getString("publisher")).thenReturn("Mock Publisher");
        when(mockResultSet.getString("genre")).thenReturn("Mock Genre");
        when(mockResultSet.getString("originalLanguage")).thenReturn("English");
        when(mockResultSet.getString("description")).thenReturn("This is a mock description.");
        when(mockResultSet.getString("thumbnail")).thenReturn("mock_thumbnail.png");
        when(mockResultSet.getDouble("rate")).thenReturn(4.5);
        when(mockResultSet.getInt("rateQuantities")).thenReturn(100);

        // Mock DBQuery
        DBQuery mockDBQuery = mock(DBQuery.class);
        when(mockDBQuery.getValue()).thenReturn(mockResultSet);

        // Set hành động khi DBQuery thành công
        mockDBQuery.setOnSucceeded(event -> {
            ResultSet resultSet = mockDBQuery.getValue();
            try {
                while (resultSet.next()) {
                    BookInfo bookInfo = new BookInfo(
                            resultSet.getInt("bookId"),
                            resultSet.getString("bookName"),
                            resultSet.getString("authorName"),
                            resultSet.getInt("quantityInStock"),
                            resultSet.getDouble("leastPrice"),
                            resultSet.getDate("publishDate").toLocalDate(),
                            resultSet.getString("ISBN"),
                            resultSet.getString("publisher"),
                            resultSet.getString("genre"),
                            resultSet.getString("originalLanguage"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail"),
                            new ImageView(getClass().getResource("/IMAGES/avatar.png").toExternalForm()),
                            resultSet.getDouble("rate"),
                            resultSet.getInt("rateQuantities")
                    );
                    libraryTable.bookList.add(bookInfo);
                }
            } catch (SQLException e) {
                fail("SQLException occurred: " + e.getMessage());
            }
        });

        // Giả lập hành vi của DBQuery trong luồng
        mockDBQuery.run();

        // Kiểm tra kết quả
        assertEquals(1, libraryTable.bookList.size());
        BookInfo book = libraryTable.bookList.get(0);
        assertEquals(1, book.getBookId());
        assertEquals("Mock Book", book.getBookName());
        assertEquals("Mock Author", book.getAuthorName());
    }
}
