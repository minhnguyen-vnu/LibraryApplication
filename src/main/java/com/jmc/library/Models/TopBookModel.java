package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TopBookModel {
    private static TopBookModel topBookModel;
    private ObservableList<BookInfo> topBookList;

    private TopBookModel() {
        topBookList = FXCollections.observableArrayList();
    }

    public ObservableList<BookInfo> getTopBookList() {
        return topBookList;
    }

    public void setTopBookList(ObservableList<BookInfo> topBookList) {
        this.topBookList = topBookList;
    }

    public static TopBookModel getInstance() {
        if (topBookModel == null) topBookModel = new TopBookModel();
        return topBookModel;
    }
}
