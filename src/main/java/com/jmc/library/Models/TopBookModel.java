package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;

public class TopBookModel {
    private static TopBookModel topBookModel;
    private ObservableList<BookInfo> topBookList;
    private FlowPane flowPane;

    private TopBookModel() {
        topBookList = FXCollections.observableArrayList();
        flowPane = new FlowPane();
    }

    public ObservableList<BookInfo> getTopBookList() {
        return topBookList;
    }

    public void setTopBookList(ObservableList<BookInfo> topBookList) {
        this.topBookList = topBookList;
    }

    public FlowPane getFlowPane() {
        return flowPane;
    }

    public void setFlowPane(FlowPane flowPane) {
        this.flowPane = flowPane;
    }

    public static TopBookModel getInstance() {
        if (topBookModel == null) topBookModel = new TopBookModel();
        return topBookModel;
    }
}
