package com.jmc.library.Controllers;

import com.jmc.library.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing the authentication view, including displaying the login and sign up views.
 */
public class Authentication implements Initializable {
    public BorderPane parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().addListener(((observableValue, oldVal, newVal) -> {
            try {
                if (parent != null) {
                    switch (newVal) {
                        case "Sign Up" -> parent.setCenter(Model.getInstance().getViewFactory().getSignUpView());
                        default -> parent.setCenter(Model.getInstance().getViewFactory().getLogInView());
                    }
                } else {
                    System.err.println("Parent BorderPane is not initialized.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}
