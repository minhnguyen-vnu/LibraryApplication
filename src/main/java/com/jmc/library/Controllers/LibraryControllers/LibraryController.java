package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.LibraryTable;
import com.jmc.library.Database.DBUtlis;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.BookViewingModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LibraryController extends LibraryTable {
    public TextField search_fld;
    public Button search_btn;
    public TableColumn<BookInfo, ImageView> book_cover_tb_cl;

    protected void initialAction(){
        search_btn.setOnAction(actionEvent -> searchBook(search_fld.getText()));
        search_fld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBook(search_fld.getText());
            }
        });
    }

    private void searchBook(String text) {
        if (text.isEmpty()) {
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

    @Override
    protected void addBinding() {
        super.addBinding();
        book_cover_tb_cl.setCellValueFactory(new PropertyValueFactory<>("imageView"));
    }

    protected void addTableClickListener() {
        store_tb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                BookDisplay(newValue);
            }
        });
    }

    protected void BookDisplay(BookInfo bookInfo) {
        BookViewingModel.getInstance().setBookInfo(bookInfo);
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Book Viewing");
    }
}
