package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User {
    private String username;
    private String token;
    public ObservableList<UserBookInfo> bookList;

    public User(String username, String token) {
        this.username = username;
        this.token = token;
        this.bookList = FXCollections.observableArrayList();
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ObservableList<UserBookInfo> getBookList() {
        return bookList;
    }
}
