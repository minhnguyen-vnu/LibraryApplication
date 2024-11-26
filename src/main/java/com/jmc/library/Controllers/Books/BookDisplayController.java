package com.jmc.library.Controllers.Books;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Interface.InterfaceManager;
import com.jmc.library.Controllers.Notification.NotificationOverlay;
import com.jmc.library.Controllers.Users.CartEntityController;
import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class BookDisplayController implements Initializable {
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
    public HBox rate_holder;

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
            UserBookInfo userBookInfo = new UserBookInfo(
                    displayBook.getBookName(),
                    displayBook.getAuthorName(),
                    displayBook.getBookId(),
                    LocalDate.now(),
                    LocalDate.now(),
                    displayBook.getLeastPrice(),
                    "Pending",
                    false,
                    displayBook.getImageView()
                    );
            try {
                System.out.println(userBookInfo.getImageView());
                addBookForUser(userBookInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void setMaterialListener() {
        System.out.println(displayBook.getISBN());
        if (displayBook.isExist()) {
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

            book_img.setImage(displayBook.getImageView().getImage());
        }
    }

    public void addBookForUser(UserBookInfo addedBook) throws IOException {
        System.out.println(addedBook.getBookName());
        if(LibraryModel.getInstance().getUser().getCartEntityControllers().stream()
                .anyMatch(cartEntityController -> cartEntityController
                        .getUserBookInfo().getBookId() == addedBook.getBookId())) {
            NotificationOverlay overlay = new NotificationOverlay("Book already in cart.", get_book_btn.getScene());
            return;
        }

        if(LibraryModel.getInstance().getUser().getBookPendingList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())) {
            NotificationOverlay overlay = new NotificationOverlay("Book already requested.", get_book_btn.getScene());
            return;
        }

        if(LibraryModel.getInstance().getUser().getBookHiredList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())
                && LibraryModel.getInstance().getUser().getBookHiredList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getReturnDate().isAfter(LocalDate.now()))) {
            NotificationOverlay overlay = new NotificationOverlay("Book already hired.", get_book_btn.getScene());
            return;
        }

        LibraryModel.getInstance().getUser()
                .getCartEntityControllers().add(new CartEntityController(addedBook));

        Model.getInstance().getViewFactory().getUserCartController()
                .onAddCartEntity(new CartEntityController(addedBook));

        NotificationOverlay overlay = new NotificationOverlay("Book added to cart.", get_book_btn.getScene());
    }
}

