package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;

public class BookModel {
    private static BookModel bookModel;
    private static BookInfo bookInfo;

    private BookModel() {
        BookModel.bookInfo = new BookInfo();
    }

    public static synchronized BookModel getInstance(){
        if(bookModel == null) bookModel = new BookModel();
        return bookModel;
    }

    public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        BookModel.bookInfo = bookInfo;
    }
}
