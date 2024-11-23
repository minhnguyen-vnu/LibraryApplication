package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BookViewingModel {
    private static BookViewingModel bookViewingModel;
    private SimpleObjectProperty<BookInfo> bookInfo;

    private BookViewingModel() {
        bookInfo = new SimpleObjectProperty<>();
    }

    public static synchronized BookViewingModel getInstance() {
        if (bookViewingModel == null) bookViewingModel = new BookViewingModel();
        return bookViewingModel;
    }

    public BookInfo getBookInfo() {
        return bookInfo.get();
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo.set(bookInfo);
    }

    public ObjectProperty<BookInfo> bookInfoProperty() {
        return bookInfo;
    }
}