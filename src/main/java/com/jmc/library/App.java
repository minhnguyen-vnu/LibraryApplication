package com.jmc.library;

import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.SQLOutput;
import java.time.LocalDate;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        DBUpdate dbUpdate = new DBUpdate("update userRequest\n" +
                "set requestStatus = 'Returned'\n" +
                "where returnDate < ?; ", LocalDate.now());
        Thread thread = new Thread(dbUpdate);
        thread.setDaemon(true);
        thread.start();

        DBUpdate dbUpdate1 = new DBUpdate("delete from PendingRequest\n" +
                "where returnDate < ?;", LocalDate.now());
        Thread thread1 = new Thread(dbUpdate1);
        thread1.setDaemon(true);
        thread1.start();
        Model.getInstance().getViewFactory().showAuthenticationWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}