package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AdminLibraryModel {
    private static AdminLibraryModel model;
    private ObservableList<BookInfo> bookList;

    private AdminLibraryModel() {
        bookList = FXCollections.observableArrayList();
    }

    public static synchronized AdminLibraryModel getInstance() {
        if (model == null) model = new AdminLibraryModel();
        return model;
    }

    public ObservableList<BookInfo> getBookList() {
        return bookList;
    }

    public void setBookList(ObservableList<BookInfo> bookList) {
        this.bookList = bookList;
    }
}
