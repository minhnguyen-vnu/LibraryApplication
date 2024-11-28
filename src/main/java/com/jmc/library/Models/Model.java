package com.jmc.library.Models;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.View.ViewFactory;

/**
 * Model class for the application.
 */
public class Model {
    private static Model model;
    private final ViewFactory viewFactory;

    private Model() {
        this.viewFactory = new ViewFactory();
    }

    public static synchronized Model getInstance() {
        if (model == null) model = new Model();
        return model;
    }

    public ViewFactory getViewFactory() {
        return this.viewFactory;
    }
}
