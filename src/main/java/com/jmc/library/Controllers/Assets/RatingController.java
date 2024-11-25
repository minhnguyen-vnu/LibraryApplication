package com.jmc.library.Controllers.Assets;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RatingController implements Initializable {
    public ImageView star5;
    public ImageView star4;
    public ImageView star3;
    public ImageView star2;
    public ImageView star1;
    public Image emptyStar;
    public Image fullStar;
    public Image halfStar;
    public int rating = 0;
    public Button btn1;
    public Button btn2;
    public Button btn3;
    public Button btn4;
    public Button btn5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        setMaterialListener();
    }

    public boolean isRated() {
        return rating != 0;
    }
    
    private void addListener() {
        btn1.setOnAction(e -> {
            star1.setImage(fullStar);
            star2.setImage(emptyStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
            rating = 1;
        });
        btn2.setOnAction(e -> {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
            rating = 2;
        });
        btn3.setOnAction(e -> {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
            rating = 3;
        });
        btn4.setOnAction(e -> {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(fullStar);
            star5.setImage(emptyStar);
            rating = 4;
        });
        btn5.setOnAction(e -> {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(fullStar);
            star5.setImage(fullStar);
            rating = 5;
        });
    }
    
    private void setMaterialListener() {
        emptyStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/EmptyStar.png")));
        halfStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/HalfStar.png")));
        fullStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/Star.png")));
        star1.setImage(emptyStar);
        star2.setImage(emptyStar);
        star3.setImage(emptyStar);
        star4.setImage(emptyStar);
        star5.setImage(emptyStar);
        
    }

    public void disPlay(double rate) {
       // make rate is multiple of 0.5
        rate = Math.round(rate * 2) / 2.0;
        if (rate == 0) {
            star1.setImage(emptyStar);
            star2.setImage(emptyStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate == 0.5) {
            star1.setImage(halfStar);
            star2.setImage(emptyStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate == 1) {
            star1.setImage(fullStar);
            star2.setImage(emptyStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate == 1.5) {
            star1.setImage(fullStar);
            star2.setImage(halfStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate == 2) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(emptyStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate == 2.5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(halfStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate == 3) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(emptyStar);
            star5.setImage(emptyStar);
        } else if (rate == 3.5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(halfStar);
            star5.setImage(emptyStar);
        } else if (rate == 4) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(fullStar);
            star5.setImage(emptyStar);
        } else if (rate == 4.5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
            star3.setImage(fullStar);
            star4.setImage(fullStar);
            star5.setImage(halfStar);
        } else if (rate == 5) {
            star1.setImage(fullStar);
            star2.setImage(fullStar);
        }
    }
}
