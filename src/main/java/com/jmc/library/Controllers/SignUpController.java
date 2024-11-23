package com.jmc.library.Controllers;

import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.Model;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignUpController implements Initializable {
    public ChoiceBox acc_selector;
    public TextField username_su;
    public PasswordField password_su;
    public Button sign_up_btn;
    public Label error_lbl;
    public PasswordField confirm_su;
    public Label back_lbl;
    public ImageView loading_img;
    public RotateTransition rotateTransition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        setRotateTransition();
    }

    public void addListener(){
        sign_up_btn.setOnAction(actionEvent -> signUp());
        back_lbl.setOnMouseClicked(mouseEvent -> {
            Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Log In");
            error_lbl.setText("");
            username_su.clear();
            confirm_su.clear();
            password_su.clear();
        });
        username_su.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                password_su.requestFocus();
            }
        });
        password_su.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                confirm_su.requestFocus();
            }
        });
        confirm_su.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                signUp();
            }
        });

    }

    public void setRotateTransition(){
        rotateTransition = new RotateTransition();
        rotateTransition.setNode(loading_img);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(1000);
        rotateTransition.setAutoReverse(false);
        rotateTransition.play();
    }

    public void signUp(){
        loading_img.setVisible(true);
        rotateTransition.play();
        if(!Objects.equals(password_su.getText(), confirm_su.getText())){
            error_lbl.setText("Your Confirmation Password didn't match the Password");
            error_lbl.setStyle("-fx-text-fill: red");
        }
        else {
            DBQuery dbQuery = new DBQuery("select * from users where username = ?", username_su.getText());
            dbQuery.setOnSucceeded(workerStateEvent -> {
                ResultSet resultSet = dbQuery.getValue();
                try {
                    if (!resultSet.next()) {
                        DBUpdate dbUpdate = new DBUpdate("insert into users (username, password, isAdmin, registeredDate) values(?, ?, ?, ?)", username_su.getText(), password_su.getText(), 0, LocalDate.now());
                        dbUpdate.setOnSucceeded(event -> {
                            Platform.runLater(() -> {
                                System.out.println("Update Successfully");
                            });
                        });
                        dbUpdate.setOnFailed(event -> {
                            Platform.runLater(() -> {
                                System.out.println("Update failed");
                            });
                        });
                        Thread thread = new Thread(dbUpdate);
                        thread.setDaemon(true);
                        thread.start();
                        loading_img.setVisible(false);
                        rotateTransition.stop();
                        error_lbl.setText("Account is created succesfully!");
                        error_lbl.setStyle("-fx-text-fill: green");
                    } else {
                        loading_img.setVisible(false);
                        rotateTransition.stop();
                        error_lbl.setText("Account name already exists");
                        error_lbl.setStyle("-fx-text-fill: red;");
                        BorderPane.setAlignment(error_lbl, Pos.CENTER);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            dbQuery.setOnFailed(workerStateEvent -> {
                System.out.println("Query failed");
            });

            Thread thread = new Thread(dbQuery);
            thread.setDaemon(true);
            thread.start();
        }
    }
}
