package com.jmc.library.Controllers.Books;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Assets.ShowRateController;
import com.jmc.library.Controllers.Notification.NotificationOverlay;
import com.jmc.library.Controllers.Users.CartEntityController;
import com.jmc.library.Models.*;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListeners();
        if (BookModel.getInstance().getBookInfo() == null) {
            System.out.println("No book selected.");
            return;
        }
        setButtonListener();
    }

    private void setButtonListener() {
        go_to_back_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });
        get_book_btn.setOnAction(actionEvent -> {
            UserBookInfo userBookInfo = new UserBookInfo(BookModel.getInstance().getBookInfo().getBookName(),
                    BookModel.getInstance().getBookInfo().getAuthorName(),
                    BookModel.getInstance().getBookInfo().getBookId(),
                    LocalDate.now(),
                    LocalDate.now().plusDays(1),
                    BookModel.getInstance().getBookInfo().getLeastPrice(),
                    "Pending",
                    new ImageView(BookModel.getInstance().getBookInfo().getImageView().getImage())
            );
            try {
                addBookForUser(userBookInfo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void addListeners() {
        book_name_txt_flw.getChildren().setAll(new Label(BookModel.getInstance().getBookInfo().getBookName()));
        author_lbl.setText("Author: " + BookModel.getInstance().getBookInfo().getAuthorName());
        publisher_lbl.setText("Publisher: " + BookModel.getInstance().getBookInfo().getPublisher());
        isbn_lbl.setText("ISBN(13): " + BookModel.getInstance().getBookInfo().getISBN());
        publication_date_lbl.setText("Publication Date: " + BookModel.getInstance().getBookInfo().getPublishedDate().toString());
        book_genres_lbl.setText("Genres: " + BookModel.getInstance().getBookInfo().getGenre());
        original_language_lbl.setText("Original Language: " + BookModel.getInstance().getBookInfo().getOriginalLanguage().toUpperCase());
        preview_txt_flw.setEditable(false);
        preview_txt_flw.setWrapText(true);
        preview_txt_flw.setText(BookModel.getInstance().getBookInfo().getDescription());
        book_img.setImage(BookModel.getInstance().getBookInfo().getImageView().getImage());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ShowRate.fxml"));
        try {
            Node rate = loader.load();
            ShowRateController controller = loader.getController();
            controller.disPlay(BookModel.getInstance().getBookInfo().getRating());
            rate_holder.getChildren().clear();
            rate_holder.getChildren().add(rate);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        BookModel.getInstance().bookInfoProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addListeners();
            }
        });
    }

    public void addBookForUser(UserBookInfo addedBook) throws IOException {
        if(CartModel.getInstance().getUserCartInfo().getCartList().stream()
                .anyMatch(cartEntityController -> cartEntityController
                        .getUserBookInfo().getBookId() == addedBook.getBookId())) {
            NotificationOverlay overlay = new NotificationOverlay("Book already in cart.", get_book_btn.getScene());
            return;
        }

        if(LibraryModel.getInstance().getUser().getPendingBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())) {
            NotificationOverlay overlay = new NotificationOverlay("Book already requested.", get_book_btn.getScene());
            return;
        }

        if(LibraryModel.getInstance().getUser().getBorrowedBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())
                && LibraryModel.getInstance().getUser().getBorrowedBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getReturnDate().isAfter(LocalDate.now()))) {
            NotificationOverlay overlay = new NotificationOverlay("Book already hired.", get_book_btn.getScene());
            return;
        }

        CartModel.getInstance().getUserCartInfo().getCartList().add(new CartEntityController(addedBook));
        CartModel.getInstance().getUserCartInfo().AddCartEntity(new CartEntityController(addedBook));

        NotificationOverlay overlay = new NotificationOverlay("Book added to cart.", get_book_btn.getScene());
    }
}

