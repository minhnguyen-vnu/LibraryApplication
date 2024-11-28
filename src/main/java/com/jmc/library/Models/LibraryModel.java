package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.Controllers.Users.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.ResultSet;
import java.time.LocalDate;

/**
 * Model class for the library view.
 */
public class LibraryModel {
    private static LibraryModel model;
    private User user;
    private ObservableList<BookInfo> bookList;


    private LibraryModel() {
        bookList = FXCollections.observableArrayList();
        this.user = new User();
    }

    public static synchronized LibraryModel getInstance() {
        if (model == null) model = new LibraryModel();
        return model;
    }

    public ObservableList<BookInfo> getBookList() {
        return bookList;
    }

    public void setBookList(ObservableList<BookInfo> bookList) {
        this.bookList = bookList;
    }

    public User getUser() {
        return user;
    }

}
