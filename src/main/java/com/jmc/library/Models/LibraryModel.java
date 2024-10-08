package com.jmc.library.Models;

import com.jmc.library.Controllers.LibraryControllers.LibraryController;

public class LibraryModel {
    private static LibraryModel model;
    private final LibraryController libraryController;

    private LibraryModel(){
        this.libraryController = new LibraryController();
    }

    public static synchronized LibraryModel getInstance(){
        if(model == null) model = new LibraryModel();
        return model;
    }

    public LibraryController getLibraryController(){
        return libraryController;
    }
}
