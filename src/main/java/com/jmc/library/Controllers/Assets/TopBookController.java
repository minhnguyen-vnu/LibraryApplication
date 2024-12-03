package com.jmc.library.Controllers.Assets;

import com.jmc.library.Assets.BookInfo;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class TopBookController implements Initializable {

    public ImageView book_cover_img;
    public Label book_author_lbl;
    public Button get_book_btn;
    public Label book_name_text;

    private BookInfo bookInfo;

    public TopBookController() {
    }

    public void setBookInfo(BookInfo bookInfo) {
        this.bookInfo = bookInfo;
        book_name_text.setWrapText(true);
        book_name_text.setText(bookInfo.getBookName());
        book_cover_img.setImage(bookInfo.getImageView().getImage());
        book_author_lbl.setText(bookInfo.getAuthorName());

        get_book_btn.setOnAction(actionEvent -> handleGetBookButton());
    }

    private void handleGetBookButton() {
        System.out.println(this.bookInfo);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialization logic if needed
    }
}