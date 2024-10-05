package com.jmc.library.Controllers;

import com.jmc.library.DBUtlis;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {addListener();}

    public void addListener(){
        sign_up_btn.setOnAction(actionEvent -> signUp());
        back_lbl.setOnMouseClicked(mouseEvent -> {
            Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Log In");
            error_lbl.setText("");
            username_su.clear();
            confirm_su.clear();
            password_su.clear();
        });
    }

    public void signUp(){
        if(!Objects.equals(password_su.getText(), confirm_su.getText())){
            error_lbl.setText("Your Confirmation Password didn't match the Password");
            error_lbl.setStyle("-fx-text-fill: red");
        }
        else {
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            Connection con = DBUtlis.getConnection();
            try {
                PreparedStatement checking = con.prepareStatement("select * from users where username = ?");
                checking.setString(1, username_su.getText());
                resultSet = checking.executeQuery();

                if (!resultSet.next()) {
                    preparedStatement = con.prepareStatement("insert into users values(?, ?)");
                    preparedStatement.setString(1, username_su.getText());
                    preparedStatement.setString(2, password_su.getText());
                    preparedStatement.executeUpdate();
                    error_lbl.setText("Account is created succesfully!");
                    error_lbl.setStyle("-fx-text-fill: green");
                } else {
                    error_lbl.setText("Account name already exists");
                    error_lbl.setStyle("-fx-text-fill: red;");
                    BorderPane.setAlignment(error_lbl, Pos.CENTER);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (con != null) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
