package com.jmc.library.Controllers.Users;

import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class UserView implements Initializable {
    public BorderPane user_parent;
//    ObservableList<BookInfo>

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSelectedUserMode().addListener(((observableValue, oldVal, newVal) -> {
            try {
                if (user_parent != null) {
                    switch (newVal) {
                        case "User Library" -> user_parent.setCenter(Model.getInstance().getViewFactory().getUserHiredBook());
                        default -> user_parent.setCenter(Model.getInstance().getViewFactory().getUserLibrary());
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
