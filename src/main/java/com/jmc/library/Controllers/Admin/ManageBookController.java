package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.GoogleBookInfo;
import com.jmc.library.Assets.LibraryTable;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private boolean isFilled() {
        boolean ok = true;
        if (enter_book_id_txt_fld.getText().isEmpty() || enter_quantity_txt_fld.getText().isEmpty() ||
                enter_price_txt_fld.getText().isEmpty() || enter_ISBN_txt_fld.getText().isEmpty()) ok = false;
        return ok;
    }

    private boolean isNotExisting() {
        AtomicBoolean ok = new AtomicBoolean(isFilled());
        try {
            int number = Integer.parseInt(enter_book_id_txt_fld.getText());
        } catch (Exception e) {
            ok.set(false);
        }

        DBQuery dbQuery = new DBQuery("select * from bookStore\n" +
                "where bookId = ? or bookName = ?; ", enter_book_id_txt_fld.getText(), enter_ISBN_txt_fld.getText());
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                if (resultSet.next()) ok.set(false);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();

        return ok.get();
    }

    private boolean isExist() {
        boolean ok = isFilled();

        try {
            int number = Integer.parseInt(enter_book_id_txt_fld.getText());
        } catch (Exception e) {
            ok = false;
        }

        return ok;
    }

    private void clear() {
        enter_ISBN_txt_fld.clear();
        enter_book_id_txt_fld.clear();
        enter_quantity_txt_fld.clear();
        enter_price_txt_fld.clear();
    }

    private void assign(BookInfo oldValue, BookInfo newValue) {
        oldValue.setBookName(newValue.getBookName());
        oldValue.setAuthorName(newValue.getAuthorName());
        oldValue.setQuantityInStock(newValue.getQuantityInStock());
        oldValue.setLeastPrice(newValue.getLeastPrice());
        oldValue.setPublishedDate(newValue.getPublishedDate());
    }

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
                            googleBookInfo.getLeastPrice(), googleBookInfo.getPublishedDate(), googleBookInfo.getISBN());
                    DBUpdate dbUpdate = new DBUpdate("insert into bookStore (bookId, bookName, publishDate, authorName, quantityInStock, leastPrice, ISBN) \n" +
                            "values (?, ?, ?, ?, ?, ?, ?);", bookInfo.getBookId(), bookInfo.getBookName(),
                            bookInfo.getPublishedDate(), bookInfo.getAuthorName(), bookInfo.getQuantityInStock(),
                            bookInfo.getLeastPrice(), bookInfo.getISBN());
                    Thread thread = new Thread(dbUpdate);
                    thread.setDaemon(true);
                    thread.start();
                    bookList.add(bookInfo);
                }

                clear();
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
            }
        });

//        update_book_btn.setOnAction(actionEvent -> {
//            boolean ok = isFilled();
//
//            if (ok) {
//                BookInfo bookInfo = new BookInfo(Integer.parseInt(enter_book_id_txt_fld.getText()), enter_book_name_txt_fld.getText(),
//                        enter_author_name_txt_fld.getText(), Integer.parseInt(enter_quantity_txt_fld.getText()),
//                        Double.parseDouble(enter_price_txt_fld.getText()), LocalDate.parse(enter_published_date_txt_fld.getText()));
//                DBUtlis.executeUpdate("update bookStore\n" +
//                        "set bookName = ?, authorName = ?, quantityInStock = ?, leastPrice = ?, publishDate = ?\n" +
//                        "where bookId = ?; \n", bookInfo.getBookName(),
//                        bookInfo.getAuthorName(), bookInfo.getQuantityInStock(),
//                        bookInfo.getLeastPrice(), Date.valueOf(bookInfo.getPublishedDate()), bookInfo.getBookId());
//                for (int i = 0; i < bookList.size(); i++) {
//                    BookInfo temp = bookList.get(i);
//                    if (temp.getBookId() == bookInfo.getBookId()) {
//                        assign(temp, bookInfo);
//                        break;
//                    }
//                }
//                store_tb.refresh();
//                clear();
//            }
//        });

        clear_btn.setOnAction(actionEvent -> clear());
    }
}
