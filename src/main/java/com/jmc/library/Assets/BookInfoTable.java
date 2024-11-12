package com.jmc.library.Assets;

import com.jmc.library.DBUtlis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class BookInfoTable {
    public TableColumn<BookInfo, Integer> book_id_tb_cl;
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public TableColumn<BookInfo, String> author_tb_cl;
    public TableColumn<BookInfo, Integer> quantity_tb_cl;
    public TableColumn<BookInfo, Double> least_price_tb_cl;
    public TableColumn<BookInfo, LocalDate> published_date_tb_cl;
    public TableView<BookInfo> store_tb;
    public ObservableList<BookInfo> bookList;

    protected void setTable() {
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);
    }

    protected void addBinding() {
        book_id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        quantity_tb_cl.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        least_price_tb_cl.setCellValueFactory(new PropertyValueFactory<>("leastPrice"));
        published_date_tb_cl.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
    }

    protected void showLibrary() {
        bookList.clear();
        store_tb.setItems(bookList);
        try {
            ResultSet resultSet = DBUtlis.executeQuery("SELECT * " +
                    "FROM bookStore b" + ";");

            while (resultSet.next()) {
                    BookInfo book = new BookInfo(resultSet.getInt("bookId"), resultSet.getString("bookName"),
                            resultSet.getDate("publishDate").toLocalDate(), resultSet.getString("authorName"),
                            resultSet.getInt("quantityInStock"), resultSet.getDouble("leastPrice"),
                            resultSet.getString("ISBN"));
                    bookList.add(book);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
