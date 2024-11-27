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

/**
 * Controller class for managing the book display view (for admin), including displaying the book's information and preview.
 */
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

    /**
     * Initializes the controller and sets up the initial state.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onAction();
        setValue();
        addListeners();
    }

    /**
     * Sets the value of the book information to be displayed.
     */
    private void setValue() {
        BookInfo bookInfo = BookViewingModel.getInstance().getBookInfo();
        if (bookInfo != null) {
            book_img.setImage(bookInfo.getImageView().getImage());
            preview_txt_flw.textProperty().set(bookInfo.getDescription());
        }
    }

    /**
     * Sets the action to be executed when the button is clicked.
     */
    private void onAction() {
        go_to_back_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View"));
    }

    /**
     * Adds listeners to the book information.
     */
    private void addListeners() {
        BookViewingModel.getInstance().bookInfoProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                book_img.setImage(newValue.getImageView().getImage());
                preview_txt_flw.textProperty().set(newValue.getDescription());
            }
        });
    }
}