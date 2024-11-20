package com.jmc.library.Controllers.Admin;

import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminView implements Initializable {
    public BorderPane admin_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getSelectedAdminMode().addListener(((observableValue, oldVal, newVal) -> {
            try {
                System.out.println(newVal);
                if (admin_parent != null) {
                    switch (newVal) {
                        case "Admin Library View" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminLibrary());
                        case "Admin Dashboard View" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminDashboard());
                        case "Admin Request Management" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminRequestManagement());
                        case "Admin Pending Request Management" -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminPendingRequestManagement());
                        default -> admin_parent.setCenter(Model.getInstance().getViewFactory().getAdminSettings());
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
