package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UserPendingController extends User implements Initializable {
    public Button back_to_dashboard_btn;
    public Button go_to_library_btn;
    public Button go_to_store_btn;
    public Button cart_btn;
    public Button pending_btn;
    public Button go_to_setting_btn;
    public Button log_out_btn;
    public Label username_lbl;
    public ImageView account_avatar_img;
    public Button search_btn;
    public TextField search_fld;
    public TableColumn<UserBookInfo, String> book_name_tb_cl;
    public TableColumn<UserBookInfo, String> author_tb_cl;
    public TableColumn<UserBookInfo, Integer> book_id_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> return_day_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> picked_day_tb_cl;
    public Button go_to_dashboard_btn;
    public ChoiceBox<Integer> num_row_shown;
    public TableView<UserBookInfo> store_tb;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        priceFormating();
        setButtonListener();
    }

    private void setButtonListener() {

    }

    private void addBinding() {

    }

    private void priceFormating() {

    }
}