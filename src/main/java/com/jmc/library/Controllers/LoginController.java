package com.jmc.library.Controllers;

import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Database.DBUtlis;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.application.Platform;
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

    public void onSignUp() {
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Sign Up");
    }

    void addListener() {
        welcome_text_txt_flw.getChildren().add(new Label("Welcome to Library\nManagement System"));
        login_btn.setOnAction(event -> login()); /// bat dau quay o day
        signup_lbl.setOnMouseClicked(mouseEvent -> onSignUp());
    }

    public void login() {
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
                            LibraryModel.getInstance().setUser(acc_address_fld.getText(), password_fld.getText());
                            LibraryModel.getInstance().getUser().loadBookList();
                        });
                    }
                    Platform.runLater(() -> stageTransforming(isAdmin));
                } else {
                    Platform.runLater(() -> {
                        error_lbl.setText("Login Failed"); /// ket thuc quay o day, neu mat khau sai
                        error_lbl.setStyle("-fx-text-fill: red");
                    });
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                DBUtlis.closeResources(null, resultSet, null);
            }
        });

        dbQuery.setOnFailed(event -> {
            System.out.println("Failed");
        });

        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    public void stageTransforming(boolean isAdmin) {

        /// mat khau dung thi ket thuc o day
        Stage currentStage = (Stage) login_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        if (isAdmin) {
            Model.getInstance().getViewFactory().showAdminWindow();
            Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View");
        }
        else {
            Model.getInstance().getViewFactory().showUserWindow();
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        }
    }
}
