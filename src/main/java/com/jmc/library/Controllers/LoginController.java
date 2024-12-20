package com.jmc.library.Controllers;

import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Database.DBUtlis;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.application.Platform;
import javafx.animation.RotateTransition;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller class for managing the login view, including user authentication and login.
 */
public class LoginController implements Initializable {
    public ChoiceBox acc_selector;
    public TextField acc_address_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public Label signup_lbl;
    public TextFlow welcome_text_txt_flw;
    public ImageView loading_img;
    private RotateTransition rotateTransition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        setRotateTransition();
    }

    /**
     * Sets the listener for the sign-up button.
     */
    public void onSignUp() {
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Sign Up");
    }

    /**
     * Adds listeners for the login button, sign-up label, and text fields.
     */
    void addListener() {
        welcome_text_txt_flw.getChildren().add(new Label("Welcome to Library\nManagement System"));
        login_btn.setOnAction(event -> login()); /// bat dau quay o day
        signup_lbl.setOnMouseClicked(mouseEvent -> onSignUp());
        acc_address_fld.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                password_fld.requestFocus();
            }
        });
        password_fld.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                login();
            }
        });
    }

    /**
     * Sets the rotation transition for the loading image.
     */
    private void setRotateTransition() {
        rotateTransition = new RotateTransition();
        rotateTransition.setNode(loading_img);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.setAutoReverse(false);
    }

    /**
     * Authenticates the user and logs in.
     */
    public void login() {
        loading_img.setVisible(true);
        rotateTransition.play();
        DBQuery dbQuery = new DBQuery("select * from users where username = ? and password = ?", acc_address_fld.getText(), password_fld.getText());
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                if (resultSet.next()) {
                    boolean isAdmin = resultSet.getBoolean("isAdmin");
                    if (!isAdmin) {
                        Platform.runLater(() -> {
                            error_lbl.setText("Login Successfully");
                            error_lbl.setStyle("-fx-text-fill: green");
                            error_lbl.setAlignment(Pos.CENTER_LEFT);
                        });
                        LibraryModel.getInstance().getUser().loadUserInfo(resultSet);
                        LibraryModel.getInstance().getUser().loadPendingBooks();
                        LibraryModel.getInstance().getUser().loadBorrowedBook();
                        DBUpdate dbUpdate = new DBUpdate("update users\n" +
                                "set status = ?\n" +
                                "where username = ?;", "online", LibraryModel.getInstance().getUser().getUsername());
                        Thread thread = new Thread(dbUpdate);
                        thread.setDaemon(true);
                        thread.start();
                    }
                    acc_address_fld.clear();
                    password_fld.clear();
                    Platform.runLater(() -> {
                        stageTransforming(isAdmin);
                        error_lbl.setText("");
                    });
                } else {
                    Platform.runLater(() -> {
                        error_lbl.setText("Login Failed");
                        loading_img.setVisible(false);
                        rotateTransition.stop();
                        error_lbl.setStyle("-fx-text-fill: red");
                    });
                    DBUtlis.closeResources(null, resultSet, null);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        dbQuery.setOnFailed(event -> {
            System.out.println("Failed");
        });

        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Transforms the stage to the user or admin view.
     *
     * @param isAdmin True if the user is an admin, false otherwise.
     */
    public void stageTransforming(boolean isAdmin) {
        loading_img.setVisible(false);
        rotateTransition.stop();
        Stage currentStage = (Stage) login_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("");
        if (isAdmin) {
            Model.getInstance().getViewFactory().showAdminWindow();
            Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View");
        } else {
            Model.getInstance().getViewFactory().showUserWindow();
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Restart");
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        }
    }
}
