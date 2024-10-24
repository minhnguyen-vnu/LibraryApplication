package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.DBUtlis;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.stream.Collectors;

public class LibraryController {
    public TextField search_fld;
    public TableColumn<BookInfo, Integer> book_id_tb_cl;
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public TableColumn<BookInfo, String> author_tb_cl;
    public TableColumn<BookInfo, Integer> quantity_tb_cl;
    public TableColumn<BookInfo, Double> least_price_tb_cl;
    public TableColumn<BookInfo, LocalDate> published_date_tb_cl;
    public TableView<BookInfo> store_tb;
    public ObservableList<BookInfo> bookList;
    public Button search_btn;

    protected void initialAction(){
        search_btn.setOnAction(actionEvent -> searchBook(search_fld.getText()));
    }

    protected void addBinding(){
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);

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
            ResultSet resultSet = DBUtlis.executeQuery("select * from bookStore");
            while (resultSet.next()) {
                BookInfo currentBook = new BookInfo(resultSet.getInt("bookId"), resultSet.getString("bookName"),
                        resultSet.getString("authorName"), resultSet.getInt("quantityInStock"), resultSet.getInt("leastPrice"),
                        resultSet.getDate("publishDate").toLocalDate(), resultSet.getString("ISBN"));
                bookList.add(currentBook);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void searchBook(String text) {
        if (text.isEmpty()) {
            System.out.println(1);
            showLibrary();
        } else {
            if (text.charAt(0) != '#') searchBookByName(text);
            else searchBookById(text.substring(1));
        }

    }

    private void searchBookByName(String text) {
        ObservableList<BookInfo> filteredList = bookList.stream()
                .filter(book -> book.getBookName().equalsIgnoreCase(text))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
        store_tb.getItems().addListener((ListChangeListener<BookInfo>) change -> {
            if (!change.next() || change.wasAdded() || change.wasRemoved()) {
                filteredList.clear();
            }
        });
    }


    private void searchBookById(String text) {
        if(text.contains(" ")){
            return;
        }

        int bookId = Integer.parseInt(text);
        ObservableList<BookInfo> filteredList = bookList.stream()
                .filter(book -> book.getBookId() == bookId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
        store_tb.getItems().addListener((ListChangeListener<BookInfo>) change -> {
            if (!change.next() || change.wasAdded() || change.wasRemoved()) {
                filteredList.clear();
            }
        });
    }
}
