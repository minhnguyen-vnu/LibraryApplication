package com.jmc.library.Controllers.Users;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class CartEntityController {
    /**
     * this parameter is used to store the number of days that the user wants to borrow the book
     * */
    private int day_borrow;
    public ImageView book_img;
    public Label book_name_lbl;
    public Label author_lbl;
    public Button decrease_btn;
    public Button increase_btn;
    public Label cost_lbl;
    public Button erase_btn;
    /**
     * this parameter is used to show int day_borrow in the view
     * */
    public TextField num_day_borrow_txt_flw;
}
