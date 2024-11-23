package com.jmc.library.Controllers;

import com.jmc.library.DataBase.*;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    public ChoiceBox acc_selector;
    public TextField acc_address_fld;
    public PasswordField password_fld;
    public Button login_btn;
    public Label error_lbl;
    public Label signup_lbl;
    public Button register;
    public TextFlow welcome_text_txt_flw;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
    }

    public void onSignUp(){
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Sign Up");
    }

    void addListener(){
        welcome_text_txt_flw.getChildren().add(new Label("Welcome to the Library"));
        login_btn.setOnAction(event -> login());
        signup_lbl.setOnMouseClicked(mouseEvent -> onSignUp());
    }

    public void login(){
        DBQuery dbQuery = new DBQuery("select * from users where username = ? and password = ?", acc_address_fld.getText(), password_fld.getText());
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                if(resultSet.next()){
                    error_lbl.setText("Login Successfully");
                    error_lbl.setStyle("-fx-text-fill: green");
                    error_lbl.setAlignment(Pos.CENTER_LEFT);
                    LibraryModel.getInstance().setUser(acc_address_fld.getText(), password_fld.getText());
                    LibraryModel.getInstance().getUser().loadAllList();
                    LibraryModel.getInstance().getUser().loadUserInfo();
                    stageTransforming();
                }
                else{
                    error_lbl.setText("Login Failed");
                    error_lbl.setStyle("-fx-text-fill: red");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBUtlis.closeResources(null, resultSet, null);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.start();
    }

    public void stageTransforming() {
        Stage currentStage = (Stage) login_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().showUserWindow();
        Model.getInstance().getViewFactory().getSelectedUserMode().set("User Restart");
        Model.getInstance().getViewFactory().getSelectedUserMode().set("User Cart");
        Model.getInstance().getViewFactory().getSelectedUserMode().set("User Dashboard");
    }
}
