package com.jmc.library.Models;

import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.Controllers.Users.User;

public class LibraryModel {
    private static LibraryModel model;
    private final UserLibraryController userLibraryController;
    private final User user;

    private LibraryModel(){
        this.userLibraryController = new UserLibraryController();
        this.user = new User();
    }

    public static synchronized LibraryModel getInstance(){
        if(model == null) model = new LibraryModel();
        return model;
    }

    public UserLibraryController getLibraryController(){
        return userLibraryController;
    }

    public User getUser() {
        return user;
    }

    public void setUser(String username, String usertoken) {
        this.user.setUsername(username);
        this.user.setToken(usertoken);
    }

}
