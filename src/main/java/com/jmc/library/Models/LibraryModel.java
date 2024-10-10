package com.jmc.library.Models;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.LibraryControllers.LibraryController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LibraryModel {
    private static LibraryModel model;
    private final LibraryController libraryController;
    private String username, usertoken;
    private ObservableList<UserBookInfo> userList;

    private LibraryModel(){
        this.libraryController = new LibraryController();
        this.userList = FXCollections.observableArrayList();
    }

    public static synchronized LibraryModel getInstance(){
        if(model == null) model = new LibraryModel();
        return model;
    }

    public LibraryController getLibraryController(){
        return libraryController;
    }

    public void setUser(String username, String usertoken, ObservableList<UserBookInfo> userList) {
        this.username = username;
        this.usertoken = usertoken;
        this.userList = userList;
    }

    public String getUsername() {
        return username;
    }

    public String getUsertoken() {
        return usertoken;
    }

    public ObservableList<UserBookInfo> getUserList() {
        return userList;
    }

}
