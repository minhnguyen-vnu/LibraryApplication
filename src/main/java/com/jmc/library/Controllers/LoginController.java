package com.jmc.library.Controllers;

import com.jmc.library.DBUtlis;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox acc_selector;
    public TextField acc_address_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public Label signup_btn;
    public Button register;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    public void onSignUp(){
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Sign Up");
    }

    void addListener(){
        login_btn.setOnAction(event -> login());
        signup_btn.setOnMouseClicked(mouseEvent -> onSignUp());
    }

    public void login(){
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection con = DBUtlis.getConnection();
        try {
            preparedStatement = con.prepareStatement("select * from users where username = ? and password = ?");
            preparedStatement.setString(1, acc_address_fld.getText());
            preparedStatement.setString(2, password_fld.getText());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                error_lbl.setText("Login Successfully");
                error_lbl.setStyle("-fx-text-fill: green");
                error_lbl.setAlignment(Pos.CENTER_LEFT);
            }
            else{
                error_lbl.setText("Login Failed");
                error_lbl.setStyle("-fx-text-fill: red");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            DBUtlis.closeResources(preparedStatement, resultSet, con);
        }
    }
}
