package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class BookModel {
    private static BookModel bookModel;
    private SimpleObjectProperty<BookInfo> bookInfo;

    private BookModel() {
        bookInfo = new SimpleObjectProperty<>();
    }

    public static synchronized BookModel getInstance(){
        if(bookModel == null) bookModel = new BookModel();
        return bookModel;
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
