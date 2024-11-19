package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.BookInfoTable;
import com.jmc.library.DataBase.*;
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

public class LibraryController extends BookInfoTable {
    public TextField search_fld;
    public Button search_btn;

    protected void initialAction(){
        search_btn.setOnAction(actionEvent -> searchBook(search_fld.getText()));
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
