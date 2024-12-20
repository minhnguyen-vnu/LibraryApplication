package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.GoogleBookInfo;
import com.jmc.library.Assets.LibraryTable;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Database.DBUtlis;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.print.Book;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Controller class for managing the books in the library.
 */
public class ManageBookController extends LibraryTable implements Initializable {
    public TextField enter_book_id_txt_fld;
    public TextField enter_quantity_txt_fld;
    public TextField enter_price_txt_fld;
    public TextField enter_ISBN_txt_fld;
    public Button add_book_btn;
    public Button delete_book_btn;
    public Button update_book_btn;
    public Button return_btn;
    public Button clear_btn;
    public Button reload_btn;
    public Label noti_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        onAction();
        setTable();
        showLibrary();
    }

    @Override
    protected void setTable() {
        bookList = AdminLibraryModel.getInstance().getBookList();
        store_tb.setItems(bookList);
    }

    /**
     * return all information is filled.
     */
    private boolean isFilled() {
        return !enter_book_id_txt_fld.getText().isEmpty() && !enter_quantity_txt_fld.getText().isEmpty() &&
                !enter_price_txt_fld.getText().isEmpty() && !enter_ISBN_txt_fld.getText().isEmpty();
    }

    /**
     * return the record is not existing.
     */
    private boolean isNotExisting() {
        boolean ok = true;

        String bookId = enter_book_id_txt_fld.getText();
        String bookISBN = enter_ISBN_txt_fld.getText();

        for (BookInfo book : bookList) {
            if (book.getBookId() == Integer.parseInt(bookId) && book.getISBN().equals(bookISBN)) {
                ok = false;
                break;
            }
        }

        return ok;
    }

    /**
     * Clears the text fields.
     */
    private void clear() {
        enter_ISBN_txt_fld.clear();
        enter_book_id_txt_fld.clear();
        enter_quantity_txt_fld.clear();
        enter_price_txt_fld.clear();
    }

    /**
     * Adds the action to be executed when the button is clicked.
     */
    private void onAction() {
        return_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View"));

        store_tb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int bookId = newValue.getBookId();
                int quantityInStock = newValue.getQuantityInStock();
                double leastPrice = newValue.getLeastPrice();
                String isbn = newValue.getISBN();

                enter_book_id_txt_fld.setText(Integer.toString(bookId));
                enter_quantity_txt_fld.setText(Integer.toString(quantityInStock));
                enter_price_txt_fld.setText(Double.toString(leastPrice));
                enter_ISBN_txt_fld.setText(isbn);
            }
        });


        add_book_btn.setOnAction(actionEvent -> {
            boolean ok = isNotExisting();
            if (ok) {
                GoogleBookInfo googleBookInfo = new GoogleBookInfo(Integer.parseInt(enter_book_id_txt_fld.getText()), Integer.parseInt(enter_quantity_txt_fld.getText()),
                        Double.parseDouble(enter_price_txt_fld.getText()), enter_ISBN_txt_fld.getText());
                if (googleBookInfo.isExist()) {
                    BookInfo bookInfo = new BookInfo(googleBookInfo.getBookId(), googleBookInfo.getBookName(),
                            googleBookInfo.getAuthorName(), googleBookInfo.getQuantityInStock(),
                            googleBookInfo.getLeastPrice(), googleBookInfo.getPublishedDate(), googleBookInfo.getISBN(),
                            googleBookInfo.getPublisher(), googleBookInfo.getGenre(), googleBookInfo.getOriginalLanguage(),
                            googleBookInfo.getDescription(), googleBookInfo.getThumbnail(), googleBookInfo.getImageView());
                    DBUpdate dbUpdate = new DBUpdate("insert into bookStore (bookId, bookName, publishDate, authorName, quantityInStock, leastPrice, ISBN, publisher, genre, originalLanguage, description, thumbnail, imageView)\n" +
                            "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", bookInfo.getBookId(), bookInfo.getBookName(),
                            bookInfo.getPublishedDate(), bookInfo.getAuthorName(), bookInfo.getQuantityInStock(),
                            bookInfo.getLeastPrice(), bookInfo.getISBN(), bookInfo.getPublisher(), bookInfo.getGenre(),
                            bookInfo.getOriginalLanguage(), bookInfo.getDescription(), bookInfo.getThumbnail(), ImageUtils.imageToByteArray(bookInfo.getImageView().getImage()));
                    System.out.println(bookInfo.getPublisher() + ", " + bookInfo.getGenre() + ", " + bookInfo.getOriginalLanguage() + ", " +
                            bookInfo.getDescription() + ", " + bookInfo.getThumbnail());
                    bookInfo.getImageView().setFitHeight(75);
                    bookInfo.getImageView().setFitWidth(50);
                    Thread thread = new Thread(dbUpdate);
                    thread.setDaemon(true);
                    thread.start();
                    bookList.add(bookInfo);
                    noti_lbl.setText("Successfully");
                }
                else {
                    noti_lbl.setText("ISBN is not valid");
                }

                clear();
            }
            else {
                noti_lbl.setText("Book is already existed");
            }
        });

        delete_book_btn.setOnAction(actionEvent -> {
            boolean ok = isNotExisting();
            if (!ok) {
                BookInfo bookInfo = store_tb.getSelectionModel().getSelectedItem();
                DBUpdate dbUpdate = new DBUpdate("delete from bookStore\n" +
                        "where bookId = ? and bookName = ? and authorName = ? and quantityInStock = ? and leastPrice = ? and publishDate = ? and ISBN = ?;",
                        bookInfo.getBookId(), bookInfo.getBookName(),
                        bookInfo.getAuthorName(), bookInfo.getQuantityInStock(),
                        bookInfo.getLeastPrice(), Date.valueOf(bookInfo.getPublishedDate()), bookInfo.getISBN());
                Thread thread = new Thread(dbUpdate);
                thread.setDaemon(true);
                thread.start();
                bookList.remove(bookInfo);
                clear();
                noti_lbl.setText("Successfully");
            }
            else {
                noti_lbl.setText("Book doesn't exist");
            }
        });

        update_book_btn.setOnAction(actionEvent -> {
            boolean ok = isFilled();

            if (ok) {
                int BookId = Integer.parseInt(enter_book_id_txt_fld.getText());
                int Quantity = Integer.parseInt(enter_quantity_txt_fld.getText());
                double Price = Double.parseDouble(enter_price_txt_fld.getText());
                String ISBN = enter_ISBN_txt_fld.getText();
                DBUpdate dbUpdate = new DBUpdate("update bookStore\n" +
                        "set quantityInStock = ?, leastPrice = ?\n" +
                        "where bookId = ?; \n", Quantity, Price, BookId);
                Thread thread = new Thread(dbUpdate);
                thread.setDaemon(true);
                thread.start();
                for (BookInfo temp : bookList) {
                    if (temp.getBookId() == BookId && temp.getISBN().equals(ISBN)) {
                        temp.setQuantityInStock(Quantity);
                        temp.setLeastPrice(Price);
                        break;
                    }
                }
                store_tb.refresh();
                clear();
                noti_lbl.setText("Successfully");
            }
            else {
                noti_lbl.setText("Please fill out all the field");
            }
        });

        clear_btn.setOnAction(actionEvent -> clear());
    }
}