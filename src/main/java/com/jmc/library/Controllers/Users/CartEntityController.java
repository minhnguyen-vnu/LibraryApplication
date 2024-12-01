package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Models.CartModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;
import java.net.URL;

/**
 * Controller class for managing the cart entity, including book information and borrowing duration.
 */
public class CartEntityController implements Initializable {
    public ImageView book_img;
    public Label book_name_lbl;
    public Label author_lbl;
    public Button decrease_btn;
    public Button increase_btn;
    public Label cost_lbl;
    public Button erase_btn;
    public TextField num_day_borrow_txt_flw;

    private int day_borrow;
    private UserBookInfo userBookInfo;

    public CartEntityController() {
        this.userBookInfo = new UserBookInfo();
        this.day_borrow = 0;
    }

    public CartEntityController(UserBookInfo userBookInfo) {
        this.userBookInfo = userBookInfo;
        this.day_borrow = (int) java.time.temporal.ChronoUnit.DAYS.between(
                userBookInfo.getPickedDate(), userBookInfo.getReturnDate());
    }

    public UserBookInfo getUserBookInfo() {
        return userBookInfo;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonListener();
        setMaterialListener();
    }

    /**
     * Updates the cost label based on the number of days borrowed.
     */
    private void updCost() {
        cost_lbl.setText(String.format("%.2f", userBookInfo.getSingleCost() * day_borrow));
        userBookInfo.setReturnDate(userBookInfo.getPickedDate().plusDays(day_borrow));
        userBookInfo.setTotalCost(userBookInfo.getSingleCost() * day_borrow);
        CartModel.getInstance().getUserCartInfo().CartUpdate();
    }

    /**
     * Sets the button listeners for increasing, decreasing, and erasing the cart entity.
     */
    private void setButtonListener() {
        decrease_btn.setOnAction(actionEvent -> {
            if (day_borrow > 1) {
                day_borrow--;
                num_day_borrow_txt_flw.setText(String.valueOf(day_borrow));
                updCost();
            }
        });

        increase_btn.setOnAction(actionEvent -> {
            day_borrow++;
            num_day_borrow_txt_flw.setText(String.valueOf(day_borrow));
            updCost();
        });

        erase_btn.setOnAction(actionEvent -> {
            CartModel.getInstance().getUserCartInfo().DeleteCartEntity(this);
        });
    }

    /**
     * Sets the initial values for the cart entity fields.
     */
    private void setMaterialListener() {
        book_name_lbl.setText(userBookInfo.getBookName());
        author_lbl.setText(userBookInfo.getAuthorName());
        cost_lbl.setText(String.valueOf(userBookInfo.getSingleCost() * day_borrow));

        num_day_borrow_txt_flw.setText(String.valueOf(day_borrow));
        num_day_borrow_txt_flw.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                day_borrow = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                day_borrow = (int) java.time.temporal.ChronoUnit.DAYS.between(
                        userBookInfo.getPickedDate(), userBookInfo.getReturnDate());
            }
        });

        num_day_borrow_txt_flw.setOnAction(actionEvent -> updCost());
        book_img.setImage(userBookInfo.getImageView().getImage());
    }
}