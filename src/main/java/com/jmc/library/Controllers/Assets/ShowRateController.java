package com.jmc.library.Controllers.Assets;

import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ShowRateController implements Initializable {
    public ImageView star5;
    public ImageView star4;
    public ImageView star3;
    public ImageView star2;
    public ImageView star1;
    public Image emptyStar;
    public Image fullStar;
    public Image halfStar;
    public int rating = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMaterialListener();
    }

    public ShowRateController(double rate) {
        disPlay(rate);
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
        rate = Math.round(rate * 2) / 2.0;
        if (rate == 0) {
            star1 = new ImageView(emptyStar);
            star2 = new ImageView(emptyStar);
            star3 = new ImageView(emptyStar);
            star4 = new ImageView(emptyStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 0.5) {
            star1 = new ImageView(halfStar);
            star2 = new ImageView(emptyStar);
            star3 = new ImageView(emptyStar);
            star4 = new ImageView(emptyStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 1) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(emptyStar);
            star3 = new ImageView(emptyStar);
            star4 = new ImageView(emptyStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 1.5) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(halfStar);
            star3 = new ImageView(emptyStar);
            star4 = new ImageView(emptyStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 2) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(fullStar);
            star3 = new ImageView(emptyStar);
            star4 = new ImageView(emptyStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 2.5) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(fullStar);
            star3 = new ImageView(halfStar);
            star4 = new ImageView(emptyStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 3) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(fullStar);
            star3 = new ImageView(fullStar);
            star4 = new ImageView(emptyStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 3.5) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(fullStar);
            star3 = new ImageView(fullStar);
            star4 = new ImageView(halfStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 4) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(fullStar);
            star3 = new ImageView(fullStar);
            star4 = new ImageView(fullStar);
            star5 = new ImageView(emptyStar);
        } else if (rate == 4.5) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(fullStar);
            star3 = new ImageView(fullStar);
            star4 = new ImageView(fullStar);
            star5 = new ImageView(halfStar);
        } else if (rate == 5) {
            star1 = new ImageView(fullStar);
            star2 = new ImageView(fullStar);
        }
    }
}
