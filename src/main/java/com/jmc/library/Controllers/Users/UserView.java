package com.jmc.library.Controllers.Users;

import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing the user view, including displaying the user dashboard, library, store, cart, and pending requests.
 */
public class UserView implements Initializable {
    public BorderPane user_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSelectedUserMode().addListener(((observableValue, oldVal, newVal) -> {
            try {
                if (user_parent != null) {
                    switch (newVal) {
                        case "User Library" ->
                                user_parent.setCenter(Model.getInstance().getViewFactory().getUserBorrowedBook());
                        case "User Dashboard" ->
                                user_parent.setCenter(Model.getInstance().getViewFactory().getUserDashboard());
                        case "User Store" -> user_parent.setCenter(Model.getInstance().getViewFactory().getUserStore());
                        case "User Cart" -> user_parent.setCenter(Model.getInstance().getViewFactory().getUserCart());
                        case "User Pending" ->
                                user_parent.setCenter(Model.getInstance().getViewFactory().getUserPending());
                        case "Book Detail" -> {
                            user_parent.setCenter(Model.getInstance().getViewFactory().getBookDetail());
                        }
                        case "User Restart" -> user_parent.setCenter(null);
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
