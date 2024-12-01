package com.jmc.library.Controllers.Assets;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class TopBookController implements Initializable {

    public TextFlow book_name_txt_flow;
    public ImageView cover_book_img;
    public Label book_author_lbl;
    public Button get_book_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        book_name_txt_flow.getChildren().add(new Label("Book Name"));
        book_author_lbl.setText("Author");
    }


}
