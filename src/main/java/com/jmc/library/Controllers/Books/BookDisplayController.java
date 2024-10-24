package com.jmc.library.Controllers.Books;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.DBUtlis;
import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
        displayBook = BookModel.getInstance().getBookInfo();
        if (displayBook == null) {
            System.out.println("No book selected.");
            return;
        }
        setButtonListener();
        setMaterialListener();
    }

    private void setButtonListener() {
        go_to_back_btn.setOnAction(actionEvent -> {
            Stage currentStage = (Stage) get_book_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
            Model.getInstance().getViewFactory().showUserWindow();
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });
        get_book_btn.setOnAction(actionEvent -> {
            try {
                ResultSet resultSet = getResultSetCaseSelectBook(displayBook);
                if (resultSet.next()) {
                    // need to change into label notification.
                    System.out.println("Book already exists in the library");
                    return;
                }
                updDatabaseCaseInsertBook(displayBook);
                // need to change into label notification.
                System.out.println("Book added to the library");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    private void setMaterialListener() {
        System.out.println(displayBook.getISBN());
        JSONObject bookInfo = new GoogleBookAPIMethod().searchBook(displayBook.getISBN());
        JSONArray items = bookInfo.getJSONArray("items");
        int totalItems = new GoogleBookAPIMethod().getTotalItems(bookInfo) - 1;
        if (items != null) {
            JSONObject volumeInfo = items.getJSONObject(totalItems).getJSONObject("volumeInfo");

            JSONArray authorsArray = volumeInfo.has("authors") ? volumeInfo.getJSONArray("authors") : null;
            book_name_txt_flw.getChildren().add(new Label(displayBook.getBookName()));
            String authors = (authorsArray != null) ? Arrays.toString(authorsArray.toList().toArray()) : "N/A";
            author_lbl.setText("Author(s): " + authors.replace("[", "").replace("]", ""));
            String publisher = volumeInfo.has("publisher") ? volumeInfo.getString("publisher") : "N/A";
            publisher_lbl.setText("Publisher: " + publisher);
            isbn_lbl.setText("ISBN(13): " + displayBook.getISBN());
            String publicationDate = volumeInfo.has("publishedDate") ? volumeInfo.getString("publishedDate") : "N/A";
            publication_date_lbl.setText("Publication Date: " + publicationDate);
            if (volumeInfo.has("categories")) {
                JSONArray categoriesArray = volumeInfo.getJSONArray("categories");
                String categories = categoriesArray.join(", ").replace("\"", "");
                book_genres_lbl.setText("Categories: " + categories);
            } else {
                book_genres_lbl.setText("N/A");
            }
            String originalLanguage = volumeInfo.has("language") ? volumeInfo.getString("language") : "N/A";
            original_language_lbl.setText("Original Language: " + originalLanguage.toUpperCase());
            String description = volumeInfo.has("description") ? volumeInfo.getString("description") : "No description available";
            preview_txt_flw.setEditable(false);
            preview_txt_flw.setWrapText(true);
            preview_txt_flw.setText(description);
            String thumbnail = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : null;
            if (thumbnail != null) {
                book_img.setImage(new javafx.scene.image.Image(thumbnail));
            }
        }
    }
}

