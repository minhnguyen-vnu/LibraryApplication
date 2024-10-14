package com.jmc.library.Models;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.LibraryControllers.LibraryController;
import com.jmc.library.Controllers.Users.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LibraryModel {
    private static LibraryModel model;
    private final LibraryController libraryController;
    private final User user;

    private LibraryModel(){
        this.libraryController = new LibraryController();
        this.user = new User();
    }

    public static synchronized LibraryModel getInstance(){
        if(model == null) model = new LibraryModel();
        return model;
    }

    public LibraryController getLibraryController(){
        return libraryController;
    }

    public User getUser() {
        return user;
    }

    public void setUser(String username, String usertoken) {
        this.user.setUsername(username);
        this.user.setToken(usertoken);
    }

}
