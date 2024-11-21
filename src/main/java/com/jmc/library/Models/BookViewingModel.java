package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;

public class BookViewingModel {
    private static BookViewingModel bookViewingModel;
    private BookInfo bookInfo;

    private BookViewingModel() {
    }

    public static synchronized BookViewingModel getInstance() {
        if (bookViewingModel == null) bookViewingModel = new BookViewingModel();
        return bookViewingModel;
    }

    public static BookViewingModel getBookViewingModel() {
        return bookViewingModel;
    }

    public static void setBookViewingModel(BookViewingModel bookViewingModel) {
        BookViewingModel.bookViewingModel = bookViewingModel;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
    }
}
