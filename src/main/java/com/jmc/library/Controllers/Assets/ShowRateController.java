package com.jmc.library.Controllers.Assets;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowRateController implements Initializable {
    @FXML
    public ImageView star5;
    @FXML
    public ImageView star4;
    @FXML
    public ImageView star3;
    @FXML
    public ImageView star2;
    @FXML
    public ImageView star1;
    public Image emptyStar;
    public Image fullStar;
    public Image halfStar;
    public int rating = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMaterialListener();
    }

    public ShowRateController() {
     //   disPlay(rate);
//        setMaterialListener();
//        disPlay(rate);
    }

    private void setMaterialListener() {

        emptyStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/EmptyStar.png")));
        halfStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/HalfStar.png")));
        fullStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/Star.png")));

    }

    public void disPlay(double rate) {
        // make rate is multiple of 0.5
        System.out.println("rate: " + rate);
        rate = Math.round(rate * 2) / 2.0;
        System.out.println(rate);
        if (rate < 0.5) {
            star1.setImage(emptyStar);
            star2.setImage(emptyStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate < 1) {
            star1.setImage(halfStar);
            star2.setImage(emptyStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate < 1.5) {
            star1.setImage(fullStar);
            star2.setImage(emptyStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate < 2) {
            star1.setImage(fullStar);
            star2.setImage(halfStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate < 2.5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate < 3) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(halfStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate < 3.5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate < 4) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(halfStar);
            star5.setImage(emptyStar);
        } else if (rate < 4.5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(fullStar);
            star5.setImage(emptyStar);
        } else if (rate < 5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(fullStar);
            star5.setImage(halfStar);
        } else {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
        }
    }
}
