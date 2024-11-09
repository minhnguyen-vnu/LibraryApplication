package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import com.jmc.library.Controllers.Interface.CartUpdateListener;
import com.jmc.library.Controllers.Interface.InterfaceManager;
import com.jmc.library.DBUtlis;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.net.URL;

public class CartEntityController extends User implements Initializable {
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

    /*private CartUpdateListener cartUpdateListener;

    public void setCartUpdateListener(CartUpdateListener listener) {
        this.cartUpdateListener = listener;
    } */

    public CartEntityController() {
        this.userBookInfo = new UserBookInfo();
        this.day_borrow = 0;
    }
    public CartEntityController(UserBookInfo userBookInfo) {
        this.userBookInfo = userBookInfo;
        this.day_borrow  = (int) java.time.temporal.ChronoUnit.DAYS.between(
                userBookInfo.getPickedDate(),
                userBookInfo.getReturnDate()) + 1;
    }

    public UserBookInfo getUserBookInfo() {
        return userBookInfo;
    }

    public void setUserBookInfo(UserBookInfo userBookInfo) {
        this.userBookInfo = userBookInfo;
    }

    public int getDay_borrow() {
        return day_borrow;
    }

    public void setDay_borrow(int day_borrow) {
        this.day_borrow = day_borrow;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonListener();
        setMaterialListener();
    }

    private void updReturnDateDB(LocalDate date) throws SQLException {
        DBUtlis.executeUpdate("UPDATE userRequest SET returnDate = '" + date +
                "' WHERE bookId = " + userBookInfo.getBookId());
    }

    private void updCostDB() throws SQLException {
        DBUtlis.executeUpdate("UPDATE userRequest SET cost = " + userBookInfo.getSingleCost() * day_borrow +
                " WHERE bookId = " + userBookInfo.getBookId());
    }

    private void updCost() throws SQLException {
        cost_lbl.setText(String.valueOf(userBookInfo.getSingleCost()* day_borrow));
        userBookInfo.setReturnDate(userBookInfo.getPickedDate().plusDays(day_borrow - 1));
        userBookInfo.setTotalCost(userBookInfo.getSingleCost() * day_borrow);
        updReturnDateDB(userBookInfo.getReturnDate());
        updCostDB();
        InterfaceManager.getInstance().getCartUpdateListener().onCartUpdated();
    }

    private void setButtonListener() {
        decrease_btn.setOnAction(actionEvent -> {
            if (day_borrow > 1) {
                day_borrow--;
                num_day_borrow_txt_flw.setText(String.valueOf(day_borrow));
                try {
                    updCost();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        increase_btn.setOnAction(actionEvent -> {
            day_borrow++;
            num_day_borrow_txt_flw.setText(String.valueOf(day_borrow));
            try {
                updCost();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        erase_btn.setOnAction(actionEvent -> {
            InterfaceManager.getInstance().getCartUpdateListener().onRemoveCartEntity(this);
        });
    }

    private void setMaterialListener() {
        book_name_lbl.setText(userBookInfo.getBookName());
        author_lbl.setText(userBookInfo.getAuthorName());
        cost_lbl.setText(String.valueOf(userBookInfo.getSingleCost()* day_borrow));

        num_day_borrow_txt_flw.setText(String.valueOf(day_borrow));
        num_day_borrow_txt_flw.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                day_borrow = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {
                day_borrow = (int) java.time.temporal.ChronoUnit.DAYS.between(
                        userBookInfo.getPickedDate(),
                        userBookInfo.getReturnDate()) + 1;
            }
        });

        num_day_borrow_txt_flw.setOnAction(actionEvent -> {
            try {
                updCost();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });

        System.out.println(userBookInfo.getISBN());
        JSONObject bookInfo = new GoogleBookAPIMethod().searchBook(userBookInfo.getISBN());
        JSONArray items = bookInfo.getJSONArray("items");
        int totalItems = new GoogleBookAPIMethod().getTotalItems(bookInfo) - 1;
        if (items != null) {
            JSONObject volumeInfo = items.getJSONObject(totalItems).getJSONObject("volumeInfo");
            String thumbnail = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : null;
            if (thumbnail != null) {
                book_img.setImage(new javafx.scene.image.Image(thumbnail));
            } else {
                book_img.setImage(new javafx.scene.image.Image("https://via.placeholder.com/150"));
            }
        }
    }
}
