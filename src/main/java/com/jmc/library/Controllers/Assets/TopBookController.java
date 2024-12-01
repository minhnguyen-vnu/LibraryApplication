package com.jmc.library.Controllers.Assets;

import com.jmc.library.Assets.BookInfo;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class TopBookController implements Initializable {

    public TextFlow book_name_txt_flow;
    public ImageView book_cover_img;
    public Label book_author_lbl;
    public Button get_book_btn;

    private BookInfo bookInfo;

    public TopBookController() {
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        book_name_txt_flow.getChildren().add(new Label(bookInfo.getBookName()));
        book_cover_img.setImage(bookInfo.getImageView().getImage());
        book_author_lbl.setText(bookInfo.getAuthorName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic if needed
    }
}