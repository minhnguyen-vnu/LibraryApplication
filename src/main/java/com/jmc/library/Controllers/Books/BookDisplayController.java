package com.jmc.library.Controllers.Books;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.DataBase.*;
import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookDisplayController extends UserLibraryController implements Initializable {
    public TextFlow book_name_txt_flw;
    public Label publication_date_lbl;
    public Label author_lbl;
    public Label publisher_lbl;
    public Label isbn_lbl;
    public Label original_language_lbl;
    public Label book_genres_lbl;
    public TextArea preview_txt_flw;
    public Button get_book_btn;
    public Button go_to_back_btn;
    public ImageView book_img;

    private BookInfo displayBook;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDisplayBook(BookModel.getInstance().getBookInfo());
        if (displayBook == null) {
            System.out.println("No book selected.");
            return;
        }
        System.out.println("Book selected: " + displayBook.getBookName());
        setButtonListener();
    }

    public void setDisplayBook(BookInfo displayBook) {
        this.displayBook = displayBook;
        setMaterialListener();
    }

    private void setButtonListener() {
        go_to_back_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });
        get_book_btn.setOnAction(actionEvent -> {
            DBQuery dbQuery = getResultSetCaseSelectBook(displayBook);
            dbQuery.setOnSucceeded(event -> {
                ResultSet resultSet = dbQuery.getValue();
                try {
                    if (resultSet.next()) {
                        System.out.println("Book already exists in the library");
                        return;
                    }
                    updDatabaseCaseInsertBook(displayBook);
                    System.out.println("Book added to the library");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            Thread thread = new Thread(dbQuery);
            thread.start();
        });
    }


    private void setMaterialListener() {
        System.out.println(displayBook.getISBN());
        if (displayBook.getItems() != null) {
            book_name_txt_flw.getChildren().setAll(new Label(displayBook.getBookName()));
            author_lbl.setText("Author: " + displayBook.getAuthorName());
            publisher_lbl.setText("Publisher: " + displayBook.getPublisher());
            isbn_lbl.setText("ISBN(13): " + displayBook.getISBN());
            publication_date_lbl.setText("Publication Date: " + displayBook.getPublishedDate().toString());
            book_genres_lbl.setText("Genres: " + displayBook.getGenre());
            original_language_lbl.setText("Original Language: " + displayBook.getOriginalLanguage().toUpperCase());
            preview_txt_flw.setEditable(false);
            preview_txt_flw.setWrapText(true);
            preview_txt_flw.setText(displayBook.getDescription());

            book_img.setImage(new Image(Objects.requireNonNullElse(displayBook.getThumbnail(), getClass().getResource("/IMAGES/UnknownBookCover.png").toExternalForm())));
        }
    }
}

