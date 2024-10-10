package com.jmc.library.View;

import com.jmc.library.Controllers.Authentication;
import com.jmc.library.Controllers.Users.UserView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private BorderPane logInView;
    private BorderPane signUpView;
    private HBox userLibrary;
    private HBox userHiredBook;
    private final StringProperty selectedAuthenticatonMode;
    private final StringProperty selectedUserMode;

    public ViewFactory() {
        this.selectedAuthenticatonMode = new SimpleStringProperty("");
        this.selectedUserMode = new SimpleStringProperty("");
    }

    public StringProperty getSelectedAuthenticatonMode() {
        return selectedAuthenticatonMode;
    }

    public StringProperty getSelectedUserMode() {
        return selectedUserMode;
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

    public HBox getUserLibrary() {
        if (userLibrary == null) {
            try {
                userLibrary = new FXMLLoader(getClass().getResource("/FXML/Library.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userLibrary;
    }

    public HBox getUserHiredBook() {
        if (userHiredBook == null) {
            try {
                userHiredBook = new FXMLLoader(getClass().getResource("/FXML/HiredBook.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userHiredBook;
    }

    public void showUserWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/UserView.fxml"));
        UserView user = new UserView();
        fxmlLoader.setController(user);
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
