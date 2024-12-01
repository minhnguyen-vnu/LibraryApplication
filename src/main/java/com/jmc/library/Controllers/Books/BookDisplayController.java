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

/**
 * Controller class for managing the book display view, including displaying the book's information and preview.
 */
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
    public ShowRateController showRateController;
    public Node rate;
    public Label qr_lbl;

    /**
     * Initializes the controller and sets up the initial state.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Book Display Controller Initialized");
        prepareListener();
        addListeners();
        if (BookModel.getInstance().getBookInfo() == null) {
            System.out.println("No book selected.");
            return;
        }
        setButtonListener();
    }

    /**
     * Loads the rating FXML file.
     */
    private void prepareListener() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/ShowRate.fxml"));
        try {
            rate = loader.load();
            showRateController  = loader.getController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        BookModel.getInstance().bookInfoProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                addListeners();
            }
        });
    }

    /**
     * Sets the action to be executed when the button is clicked.
     */
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

    /**
     * Adds listeners to the book information.
     */
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


        showRateController.disPlay(BookModel.getInstance().getBookInfo().getRating());

        rate_holder.getChildren().clear();
        rate_holder.getChildren().add(rate);

        qr_lbl.setText("QR Code: " + BookModel.getInstance().getBookInfo().getISBN());
        qr_lbl.setOnMouseClicked(mouseEvent -> {
            try {
                NotificationOverlay.QRScreen(qr_lbl.getScene());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Adds a book to the user's cart.
     *
     * @param addedBook The book to add.
     * @throws IOException If the FXML file for the notification overlay cannot be found.
     */
    public void addBookForUser(UserBookInfo addedBook) throws IOException {
        if(CartModel.getInstance().getUserCartInfo().getCartList().stream()
                .anyMatch(cartEntityController -> cartEntityController
                        .getUserBookInfo().getBookId() == addedBook.getBookId())) {
            NotificationOverlay.notificationScreen("Book already in cart.", get_book_btn.getScene());
            return;
        }

        if(LibraryModel.getInstance().getUser().getPendingBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())) {
            NotificationOverlay.notificationScreen("Book already requested.", get_book_btn.getScene());
            return;
        }

        if(LibraryModel.getInstance().getUser().getBorrowedBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())
                && LibraryModel.getInstance().getUser().getBorrowedBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getReturnDate().isAfter(LocalDate.now()))) {
            NotificationOverlay.notificationScreen("Book already borrowed.", get_book_btn.getScene());
            return;
        }

        CartModel.getInstance().getUserCartInfo().getCartList().add(new CartEntityController(addedBook));
        CartModel.getInstance().getUserCartInfo().AddCartEntity(new CartEntityController(addedBook));

        NotificationOverlay.notificationScreen("Book added to cart.", get_book_btn.getScene());
    }
}

