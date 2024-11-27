package com.jmc.library.Controllers.Users;

import javafx.animation.ScaleTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Manages the user information overlay, including displaying and closing the overlay with animations.
 */
public class UserInfoOverlay {
    private final AnchorPane overlayPane;
    private final AnchorPane userInfoPane;

    /**
     * Constructs a UserInfoOverlay and initializes the overlay pane.
     *
     * @param currentScene The current scene where the overlay will be displayed.
     * @throws IOException If loading the FXML file fails.
     */
    public UserInfoOverlay(Scene currentScene) throws IOException {
        Pane root = (Pane) currentScene.getRoot();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UserInfo.fxml"));
        userInfoPane = loader.load();
        UserInfoController userInfoController = loader.getController();

        overlayPane = new AnchorPane(userInfoPane);
        overlayPane.getStyleClass().add("MatteBackground");

        double senceWidth = currentScene.getWidth();
        double senceHeight = currentScene.getHeight();
        double overlayWidth = userInfoPane.getPrefWidth();
        double overlayHeight = userInfoPane.getPrefHeight();

        AnchorPane.setTopAnchor(userInfoPane, senceHeight / 2 - overlayHeight / 2);
        AnchorPane.setRightAnchor(userInfoPane, senceWidth / 2 - overlayWidth / 2);
        AnchorPane.setBottomAnchor(userInfoPane, senceHeight / 2 - overlayHeight / 2);
        AnchorPane.setLeftAnchor(userInfoPane, senceWidth / 2 - overlayWidth / 2);

        playZoomInEffect(userInfoPane);

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

            Bounds boundsInScene = userInfoPane.localToScene(userInfoPane.getBoundsInLocal());

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
    private void closeOverlay(Scene currentScene) {
        playZoomOutEffect(userInfoPane, () -> {
            Pane root = (Pane) currentScene.getRoot();
            root.getChildren().remove(overlayPane);
        });
    }

    /**
     * Plays a zoom-in effect on the specified pane.
     *
     * @param pane The pane to apply the zoom-in effect to.
     */
    private void playZoomInEffect(AnchorPane pane) {
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
     * @param pane       The pane to apply the zoom-out effect to.
     * @param onFinished The action to run upon completion of the zoom-out effect.
     */
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
