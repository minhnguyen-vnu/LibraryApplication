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

public class NotificationOverlay {
    private final AnchorPane overlayPane;
    private final AnchorPane notificationPane;

    public NotificationOverlay(String notificationMessage, Scene currentScene) throws IOException {
        Pane root = (Pane) currentScene.getRoot();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Notification.fxml"));
        notificationPane = loader.load();
        NotificationController notificationController = loader.getController();

        notificationController.setNotification(notificationMessage);
        notificationController.setOnCloseCallback(() -> closeOverlay(currentScene));

        overlayPane = new AnchorPane(notificationPane);
        overlayPane.getStyleClass().add("MatteBackground");

        double senceWidth = currentScene.getWidth();
        double senceHeight = currentScene.getHeight();
        double overlayWidth = notificationPane.getPrefWidth();
        double overlayHeight = notificationPane.getPrefHeight();

        System.out.println("senceWidth: " + senceWidth);
        System.out.println("senceHeight: " + senceHeight);
        System.out.println("overlayWidth: " + overlayWidth);
        System.out.println("overlayHeight: " + overlayHeight);

        AnchorPane.setTopAnchor(notificationPane, senceHeight / 2 - overlayHeight / 2);
        AnchorPane.setRightAnchor(notificationPane, senceWidth / 2 - overlayWidth / 2);
//        AnchorPane.setBottomAnchor(notificationPane, senceHeight / 2 - overlayHeight / 2);
//        AnchorPane.setLeftAnchor(notificationPane, senceWidth / 2 - overlayWidth / 2);

        playZoomInEffect(notificationPane);

        if (!(root instanceof StackPane)) {
            StackPane stackPane = new StackPane(root);
            currentScene.setRoot(stackPane);
            root = stackPane;
        }

        root.getStylesheets().add(getClass().getResource("/STYLES/Stuff.css").toExternalForm());
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
