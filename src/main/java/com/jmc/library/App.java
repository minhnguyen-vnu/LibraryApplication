package com.jmc.library;

import com.jmc.library.Models.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLOutput;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Model.getInstance().getViewFactory().showUserWindow();
        Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
    }

    public static void main(String[] args) {
        launch();
    }
}