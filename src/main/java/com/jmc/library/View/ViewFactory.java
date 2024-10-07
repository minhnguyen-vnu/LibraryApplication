package com.jmc.library.View;

import com.jmc.library.Controllers.Authentication;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private BorderPane logInView;
    private BorderPane signUpView;
    private final StringProperty selectedAuthenticatonMode;

    public ViewFactory() {
        this.selectedAuthenticatonMode = new SimpleStringProperty("");
    }

    public StringProperty getSelectedAuthenticatonMode() {
        return selectedAuthenticatonMode;
    }

    public BorderPane getSignUpView() {
        if (signUpView == null) {
            try {
                signUpView = new FXMLLoader(getClass().getResource("/FXML/SignUp.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return signUpView;
    }

    public BorderPane getLogInView() {
        if (logInView == null) {
            try {
                logInView = new FXMLLoader(getClass().getResource("/FXML/Login.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logInView;
    }

    public void showAuthenticationWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Authentication.fxml"));
        Authentication authentication = new Authentication();
        fxmlLoader.setController(authentication);
        createStage(fxmlLoader);
    }

    public void showUserLibrary() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/UserLibrary.fxml"));
        createStage(fxmlLoader);
    }

    public void showUserHiredBookList() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/HiredBook.fxml"));
        createStage(fxmlLoader);
    }

    public void showAdminLibrary() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Admin.fxml"));
        createStage(fxmlLoader);
    }

    void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Library Application");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
