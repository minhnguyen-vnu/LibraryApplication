package com.jmc.library.Controllers.Notification;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class NotificationController {
    public Button cancel_btn;
    public Label notification_lbl;

    private Runnable onCloseCallback;

    public void setNotification(String notification) {
        notification_lbl.setText(notification);
    }

    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    @FXML
    public void initialize() {
        cancel_btn.setOnAction(e -> {
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });
    }
}
