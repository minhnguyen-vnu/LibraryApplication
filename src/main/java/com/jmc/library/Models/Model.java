package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.View.ViewFactory;

public class Model {
    private static Model model;
    private final ViewFactory viewFactory;
    //private static BookInfo bookInfo;

    private Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance(){
        if(model == null) model = new Model();
        return model;
    }

    public ViewFactory getViewFactory(){
        return this.viewFactory;
    }

    /*public BookInfo getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(BookInfo bookInfo) {
        Model.bookInfo = bookInfo;
    }*/
}
