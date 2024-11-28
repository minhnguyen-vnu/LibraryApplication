package com.jmc.library.Controllers.Notification;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for managing the notification display and handling user interactions.
 */
public class NotificationController {
    public Button cancel_btn;
    public Label notification_lbl;

    private Runnable onCloseCallback;

    /**
     * Sets the notification message to be displayed.
     *
     * @param notification The notification message.
     */
    public void setNotification(String notification) {
        notification_lbl.setText(notification);
    }

    /**
     * Sets the callback to be executed when the notification is closed.
     *
     * @param callback The callback to be executed.
     */
    public void setOnCloseCallback(Runnable callback) {
        this.onCloseCallback = callback;
    }

    /**
     * Initializes the controller and sets up the button action.
     */
    @FXML
    public void initialize() {
        cancel_btn.setOnAction(e -> {
            if (onCloseCallback != null) {
                onCloseCallback.run();
            }
        });
    }
}
