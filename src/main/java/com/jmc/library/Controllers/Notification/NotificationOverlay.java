package com.jmc.library.Controllers.Notification;

import com.jmc.library.Controllers.Assets.QRCodeGenerator;
import com.jmc.library.Models.BookModel;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

/**
 * Manages the notification overlay, including displaying and closing the overlay with animations.
 */
public class NotificationOverlay {
    private static AnchorPane overlayPane;
    private static AnchorPane overlayMainPane;

    /**
     * Constructs a NotificationOverlay and initializes the overlay pane.
     *
     * @param notificationMessage The message to display in the notification.
     * @param currentScene The current scene where the overlay will be displayed.
     * @throws IOException If loading the FXML file fails.
     */
    public static void notificationScreen(String notificationMessage, Scene currentScene) throws IOException {
        Pane root = (Pane) currentScene.getRoot();

        FXMLLoader loader = new FXMLLoader(NotificationOverlay.class.getResource("/FXML/Notification.fxml"));
        overlayMainPane = loader.load();
        NotificationController notificationController = loader.getController();

        notificationController.setNotification(notificationMessage);
        notificationController.setOnCloseCallback(() -> closeOverlay(currentScene));

        buildOverlay(root, currentScene);
    }

    public static void QRScreen(Scene currentScene) throws IOException {
        Pane root = (Pane) currentScene.getRoot();

        overlayMainPane = new AnchorPane();
        Image image = QRCodeGenerator.getQRCodeImage();
        overlayMainPane.getChildren().add(new ImageView(image));

        buildOverlay(root, currentScene);
    }

    private static void buildOverlay(Pane root, Scene currentScene) throws IOException {
        overlayPane = new AnchorPane(overlayMainPane);
        overlayPane.getStyleClass().add("MatteBackground");

        double sceneWidth = currentScene.getWidth();
        double scene = currentScene.getHeight();
        double overlayWidth = overlayMainPane.getPrefWidth();
        double overlayHeight = overlayMainPane.getPrefHeight();

        System.out.println("sceneWidth: " + sceneWidth);
        System.out.println("scene: " + scene);
        System.out.println("overlayWidth: " + overlayWidth);
        System.out.println("overlayHeight: " + overlayHeight);

        AnchorPane.setTopAnchor(overlayMainPane, scene / 2 - overlayHeight / 2);
        AnchorPane.setRightAnchor(overlayMainPane, sceneWidth / 2 - overlayWidth / 2);
        AnchorPane.setBottomAnchor(overlayMainPane, scene / 2 - overlayHeight / 2);
        AnchorPane.setLeftAnchor(overlayMainPane, sceneWidth / 2 - overlayWidth / 2);

        playZoomInEffect(overlayMainPane);

        if (!(root instanceof StackPane)) {
            StackPane stackPane = new StackPane(root);
            currentScene.setRoot(stackPane);
            root = stackPane;
        }

        root.getStylesheets().add(NotificationOverlay.class.getResource("/STYLES/Stuff.css").toExternalForm());
        root.getChildren().add(overlayPane);


        overlayPane.setOnMouseClicked(event -> {
            double clickX = event.getSceneX();
            double clickY = event.getSceneY();

            Bounds boundsInScene = overlayMainPane.localToScene(overlayMainPane.getBoundsInLocal());

            if (!boundsInScene.contains(clickX, clickY)) {
                closeOverlay(currentScene);
            }
        });
    }

    /**
     * Closes the overlay with a zoom-out effect.
     *
     * @param currentScene The current scene where the overlay is displayed.
     */
    private static void closeOverlay(Scene currentScene) {
        playZoomOutEffect(overlayMainPane, () -> {
            Pane root = (Pane) currentScene.getRoot();
            root.getChildren().remove(overlayPane);
        });
    }

    /**
     * Plays a zoom-in effect on the specified pane.
     *
     * @param pane The pane to apply the zoom-in effect to.
     */
    private static void playZoomInEffect(AnchorPane pane) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(300), pane);
        scaleTransition.setFromX(0.0);
        scaleTransition.setFromY(0.0);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);
        scaleTransition.setInterpolator(javafx.animation.Interpolator.EASE_OUT);
        scaleTransition.play();
    }

    /**
     * Plays a zoom-out effect on the specified pane and runs the specified action upon completion.
     *
     * @param pane The pane to apply the zoom-out effect to.
     * @param onFinished The action to run upon completion of the zoom-out effect.
     */
    private static void playZoomOutEffect(AnchorPane pane, Runnable onFinished) {
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
