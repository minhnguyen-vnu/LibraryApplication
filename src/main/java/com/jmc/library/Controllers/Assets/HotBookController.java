package com.jmc.library.Controllers.Assets;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class HotBookController implements Initializable {

    @FXML
    public TextFlow book_name_txt_flow;
    @FXML
    public ImageView cover_book_img;
    @FXML
    public Label book_author_lbl;
    @FXML
    public Button get_book_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        book_name_txt_flow.getChildren().add(new Label("Book Name"));
        book_author_lbl.setText("Author");
    }


}
