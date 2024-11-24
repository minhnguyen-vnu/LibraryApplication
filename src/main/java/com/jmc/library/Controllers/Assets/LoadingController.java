package com.jmc.library.Controllers.Assets;

import javafx.animation.RotateTransition;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    public ImageView loading_img;
    public RotateTransition rotate;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRotate();
    }

    public void setRotate() {
        rotate = new RotateTransition();
        rotate.setNode(loading_img);
        rotate.setByAngle(360);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.play();
    }

}