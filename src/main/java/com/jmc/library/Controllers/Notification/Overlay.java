package com.jmc.library.Controllers.Notification;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

public class Overlay {
    private final AnchorPane overlayPane;
    private final AnchorPane notificationPane;

    public Overlay(String notificationMessage, Scene currentScene) throws IOException {
        Pane root = (Pane) currentScene.getRoot();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Notification.fxml"));
        notificationPane = loader.load();
        NotificationController notificationController = loader.getController();

        notificationController.setNotification(notificationMessage);
        notificationController.setOnCloseCallback(() -> closeOverlay(currentScene));

        overlayPane = new AnchorPane(notificationPane);
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");


        AnchorPane.setTopAnchor(notificationPane, 100.0);
        AnchorPane.setRightAnchor(notificationPane, 50.0);
        AnchorPane.setBottomAnchor(notificationPane, 100.0);
        AnchorPane.setLeftAnchor(notificationPane, 50.0);

        playZoomInEffect(notificationPane);

        if (!(root instanceof StackPane)) {
            StackPane stackPane = new StackPane(root);
            currentScene.setRoot(stackPane);
            root = stackPane;
        }

        root.getChildren().add(overlayPane);

        overlayPane.setOnMouseClicked(event -> {
            double clickX = event.getSceneX();
            double clickY = event.getSceneY();

            Bounds boundsInScene = notificationPane.localToScene(notificationPane.getBoundsInLocal());

            if (!boundsInScene.contains(clickX, clickY)) {
                closeOverlay(currentScene);
            }
        });
    }

    private void closeOverlay(Scene currentScene) {
        playZoomOutEffect(notificationPane, () -> {
            Pane root = (Pane) currentScene.getRoot();
            root.getChildren().remove(overlayPane);
        });
    }

    private void playZoomInEffect(AnchorPane pane) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), pane);
        scaleTransition.setFromX(0.0);
        scaleTransition.setFromY(0.0);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        scaleTransition.play();
    }

    private void playZoomOutEffect(AnchorPane pane, Runnable onFinished) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), pane);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(0.0);
        scaleTransition.setToY(0.0);
        scaleTransition.setInterpolator(javafx.animation.Interpolator.EASE_IN);
        scaleTransition.setOnFinished(event -> {
            if (onFinished != null) {
                onFinished.run();
            }
        });
        scaleTransition.play();
    }
}
