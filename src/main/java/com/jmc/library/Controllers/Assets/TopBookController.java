package com.jmc.library.Controllers.Assets;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Notification.BookAddingNotification;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
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

        get_book_btn.setOnAction(actionEvent -> {
            try {
                handleGetBookButton();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });
    }

    private void handleGetBookButton() throws IOException {
        UserBookInfo userBookInfo = new UserBookInfo(this.bookInfo.getBookName(), this.bookInfo.getAuthorName(),
                this.bookInfo.getBookId(), LocalDate.now(), LocalDate.now().plusDays(1),
                this.bookInfo.getLeastPrice(), "Pending", new ImageView(bookInfo.getImageView().getImage()));
        BookAddingNotification.addBookForUser(userBookInfo, get_book_btn.getScene());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}