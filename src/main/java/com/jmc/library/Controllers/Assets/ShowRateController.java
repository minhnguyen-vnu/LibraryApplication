package com.jmc.library.Controllers.Assets;

import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
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

    private void setMaterialListener() {
        emptyStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/EmptyStar.png")));
        halfStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/HalfStar.png")));
        fullStar = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IMAGES/Star.png")));
        star1.setImage(emptyStar);
        star2.setImage(emptyStar);
        star3.setImage(emptyStar);
        star4.setImage(emptyStar);
        star5.setImage(emptyStar);

        BookModel.getInstance().getBookInfo().doubleProperty().addListener(((observableValue, number, t1) -> {
            disPlay(t1.doubleValue());
        }));
    }

    public void disPlay(double rate) {
        rate = Math.round(rate * 2) / 2.0;
        star1.setImage(fullStar);
        star2.setImage(fullStar);
        star3.setImage(fullStar);
        star4.setImage(fullStar);
        star5.setImage(fullStar);

        if (rate <= 4.5) {
            star5.setImage(halfStar);
        }

        if(rate <= 4) {
            star5.setImage(emptyStar);
        }

        if (rate <= 3.5) {
            star4.setImage(halfStar);
        }

        if(rate <= 3) {
            star4.setImage(emptyStar);
        }

        if (rate <= 2.5) {
            star3.setImage(halfStar);
        }

        if(rate <= 2) {
            star3.setImage(emptyStar);
        }

        if (rate <= 1.5) {
            star2.setImage(halfStar);
        }

        if(rate <= 1) {
            star2.setImage(emptyStar);
        }

        if (rate <= 0.5) {
            star1.setImage(halfStar);
        }

        if(rate <= 0) {
            star1.setImage(emptyStar);
        }

    }
}