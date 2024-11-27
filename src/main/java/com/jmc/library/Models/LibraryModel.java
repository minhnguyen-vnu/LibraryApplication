package com.jmc.library.Models;

import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.Controllers.Users.User;
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

    private LibraryModel() {
        this.user = new User();
    }

    public static synchronized LibraryModel getInstance() {
        if (model == null) model = new LibraryModel();
        return model;
    }

    public static synchronized void deleteInstace() {
        model.user = null;
        model = null;
    }

    public User getUser() {
        return user;
    }

}
