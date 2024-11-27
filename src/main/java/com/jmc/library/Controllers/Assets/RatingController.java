package com.jmc.library.Controllers.Assets;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.LibraryTable;
import com.jmc.library.Controllers.Books.BookDisplayController;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

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
    public Button accept_btn;
    public Button cancel_btn;

    private int bookId;

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getBookId() {
        return bookId;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        setMaterialListener();
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
        accept_btn.setOnAction(e -> {
            UpdateDB();
            closeStage();
        });
        cancel_btn.setOnAction(e -> closeStage());
    }

    private void closeStage() {
        star1.getScene().getWindow().hide();
    }
    private void UpdateDB() {
        System.out.println(bookId);
        DBUpdate dbUpdate = new DBUpdate("update userRequest set isRated = true where username = ? and bookId = ? and isRated = false",
                LibraryModel.getInstance().getUser().getUsername(), bookId);
        dbUpdate.setOnSucceeded(e -> {
            System.out.println("update success 1");
        });
        Thread thread = new Thread(dbUpdate);
        thread.setDaemon(true);
        thread.start();

        DBUpdate dbUpdate1 = new DBUpdate("UPDATE bookStore SET rateQuantities = rateQuantities + 1, " +
                "rate = ((rate * (rateQuantities - 1)) + ?) / rateQuantities " +
                "WHERE bookId = ?",
                rating, bookId);
        dbUpdate1.setOnSucceeded(e -> {
            System.out.println("update success 2");
            ObservableList<BookInfo> bookList = LibraryTable.bookList;
            for (BookInfo book : bookList) {
                if (book.getBookId() == bookId) {
                    book.setRateQuantities(book.getRateQuantities() + 1);
                    book.setRating((book.getRating() * (book.getRateQuantities() - 1) + rating) / book.getRateQuantities());
                    BookDisplayController.setRateHolder();
                    break;
                }
            }
        });
        Thread thread1 = new Thread(dbUpdate1);
        thread1.setDaemon(true);
        thread1.start();
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
}