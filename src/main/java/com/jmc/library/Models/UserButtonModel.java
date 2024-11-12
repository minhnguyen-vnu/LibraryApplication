package com.jmc.library.Models;

import javafx.scene.control.Button;
import javafx.stage.Stage;

public class UserButtonModel {
    private static UserButtonModel instance;

    public Button go_to_setting_btn;
    public Button go_to_store_btn;
    public Button go_to_library_btn;
    public Button go_to_dashboard_btn;
    public Button go_to_pending_btn;
    public Button go_to_cart_btn;
    public Button log_out_btn;

    private UserButtonModel() {
        go_to_setting_btn = new Button("go_to_setting_btn");
        go_to_store_btn = new Button("go_to_store_btn");
        go_to_library_btn = new Button("go_to_library_btn");
        go_to_dashboard_btn = new Button("go_to_dashboard_btn");
        go_to_pending_btn = new Button("go_to_pending_btn");
        go_to_cart_btn = new Button("go_to_cart_btn");
        log_out_btn = new Button("log_out_btn");
        setOnAction();
    }

    private void setOnAction() {
        go_to_setting_btn.setOnAction(e -> {
            System.out.println("go_to_setting_btn clicked");
        });

        go_to_store_btn.setOnAction(e -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        go_to_library_btn.setOnAction(e -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });

        go_to_dashboard_btn.setOnAction(e -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Dashboard");
        });

        go_to_pending_btn.setOnAction(e -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });

        go_to_cart_btn.setOnAction(e -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Cart");
        });

        log_out_btn.setOnAction(actionEvent -> {
            Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
            Model.getInstance().getViewFactory().showAuthenticationWindow();
        });
    }

    public static UserButtonModel getInstance() {
        if (instance == null) {
            instance = new UserButtonModel();
        }
        return instance;
    }
}
