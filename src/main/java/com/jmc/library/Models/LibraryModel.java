package com.jmc.library.Models;

import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.Account.User;

public class LibraryModel {
    private static LibraryModel model;
    private final User user;

    private LibraryModel(){
        this.user = new User();
    }

    public static synchronized LibraryModel getInstance(){
        if(model == null) model = new LibraryModel();
        return model;
    }

    public User getUser() {
        return user;
    }

    public void setUser(String username, String usertoken) {
        this.user.setUsername(username);
        this.user.setToken(usertoken);
    }

}
