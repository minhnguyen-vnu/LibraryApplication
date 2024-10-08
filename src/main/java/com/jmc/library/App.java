package com.jmc.library;

import com.jmc.library.Models.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
<<<<<<< HEAD
        Model.getInstance().getViewFactory().showAdminLibrary();
=======
        Model.getInstance().getViewFactory().showUserWindow();
>>>>>>> 6df873ca63517596ce5ef6719225ffb2de0bc8f3
    }

    public static void main(String[] args) {
        launch();
    }
}