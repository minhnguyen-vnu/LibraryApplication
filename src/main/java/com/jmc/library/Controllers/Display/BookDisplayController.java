package com.jmc.library.Controllers.Display;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Models.BookViewingModel;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.ResourceBundle;

public class BookDisplayController implements Initializable {
    public ImageView book_img;
    public Button get_book_btn;
    public TextFlow book_name_txt_flw;
    public Label publication_date_lbl;
    public Label author_lbl;
    public Label publisher_lbl;
    public Label isbn_lbl;
    public Label original_language_lbl;
    public Label book_genres_lbl;
    public TextArea preview_txt_flw;
    public Button go_to_back_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onAction();
        setValue();
        addListeners();
    }

    private void setValue() {
        BookInfo bookInfo = BookViewingModel.getInstance().getBookInfo();
        if (bookInfo != null) {
            book_img.setImage(bookInfo.getImageView().getImage());
            preview_txt_flw.textProperty().set(bookInfo.getDescription());
        }
    }

    private void onAction() {
        go_to_back_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View"));
    }

    private void addListeners() {
        BookViewingModel.getInstance().bookInfoProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                book_img.setImage(newValue.getImageView().getImage());
                preview_txt_flw.textProperty().set(newValue.getDescription());
            }
        });
    }
}