package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.LibraryTable;
import com.jmc.library.DBUtlis;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ManageBookController extends LibraryTable implements Initializable {
    public TextField enter_book_id_txt_fld;
    public TextField enter_book_name_txt_fld;
    public TextField enter_author_name_txt_fld;
    public TextField enter_quantity_txt_fld;
    public Button add_book_btn;
    public Button delete_book_btn;
    public Button update_book_btn;
    public Button return_btn;
    public TextField enter_price_txt_fld;
    public TextField enter_published_date_txt_fld;
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
        if (enter_book_id_txt_fld.getText().isEmpty() || enter_book_name_txt_fld.getText().isEmpty() ||
                enter_author_name_txt_fld.getText().isEmpty() || enter_quantity_txt_fld.getText().isEmpty() ||
                enter_price_txt_fld.getText().isEmpty() || enter_published_date_txt_fld.getText().isEmpty()) ok = false;
        return ok;
    }

    private boolean isNotExisting() {
        boolean ok = isFilled();
        try {
            int number = Integer.parseInt(enter_book_id_txt_fld.getText());
        } catch (Exception e) {
            ok = false;
        }

        try {
            ResultSet resultSet = DBUtlis.executeQuery("select * from bookStore\n" +
                    "where bookId = ? or bookName = ?; ", enter_book_id_txt_fld.getText(), enter_book_name_txt_fld.getText());
            if (resultSet.next()) ok = false;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return ok;
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
        enter_book_id_txt_fld.clear();
        enter_book_name_txt_fld.clear();
        enter_author_name_txt_fld.clear();
        enter_quantity_txt_fld.clear();
        enter_price_txt_fld.clear();
        enter_published_date_txt_fld.clear();
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
                String bookName = newValue.getBookName();
                String authorName = newValue.getAuthorName();
                int quantityInStock = newValue.getQuantityInStock();
                double leastPrice = newValue.getLeastPrice();
                LocalDate publishedDate = newValue.getPublishedDate();

                enter_book_id_txt_fld.setText(Integer.toString(bookId));
                enter_book_name_txt_fld.setText(bookName);
                enter_author_name_txt_fld.setText(authorName);
                enter_quantity_txt_fld.setText(Integer.toString(quantityInStock));
                enter_price_txt_fld.setText(Double.toString(leastPrice));
                enter_published_date_txt_fld.setText(publishedDate.toString());
            }
        });


        add_book_btn.setOnAction(actionEvent -> {
            boolean ok = isNotExisting();
            if (ok) {
                BookInfo bookInfo = new BookInfo(Integer.parseInt(enter_book_id_txt_fld.getText()), enter_book_name_txt_fld.getText(),
                        enter_author_name_txt_fld.getText(), Integer.parseInt(enter_quantity_txt_fld.getText()),
                        Double.parseDouble(enter_price_txt_fld.getText()), LocalDate.parse(enter_published_date_txt_fld.getText()));
                DBUtlis.executeUpdate("insert into bookStore (bookId, bookName, publishDate, authorName, quantityInStock, leastPrice) \n" +
                        "values (?, ?, ?, ?, ?, ?);",   bookInfo.getBookId(), bookInfo.getBookName(),
                        bookInfo.getPublishedDate(), bookInfo.getAuthorName(), bookInfo.getQuantityInStock(),
                        bookInfo.getLeastPrice());
                bookList.add(bookInfo);

                clear();
            }
        });

        delete_book_btn.setOnAction(actionEvent -> {
            boolean ok = isNotExisting();
            if (!ok) {
                BookInfo bookInfo = new BookInfo(Integer.parseInt(enter_book_id_txt_fld.getText()), enter_book_name_txt_fld.getText(),
                        enter_author_name_txt_fld.getText(), Integer.parseInt(enter_quantity_txt_fld.getText()),
                        Double.parseDouble(enter_price_txt_fld.getText()), LocalDate.parse(enter_published_date_txt_fld.getText()));
                DBUtlis.executeUpdate("delete from bookStore\n" +
                        "where bookId = ? and bookName = ? and authorName = ? and quantityInStock = ? and leastPrice = ? and publishDate = ?;",
                        bookInfo.getBookId(), bookInfo.getBookName(),
                        bookInfo.getAuthorName(), bookInfo.getQuantityInStock(),
                        bookInfo.getLeastPrice(), Date.valueOf(bookInfo.getPublishedDate()));
                bookList.remove(bookInfo);
            }
        });

        update_book_btn.setOnAction(actionEvent -> {
            boolean ok = isFilled();

            if (ok) {
                BookInfo bookInfo = new BookInfo(Integer.parseInt(enter_book_id_txt_fld.getText()), enter_book_name_txt_fld.getText(),
                        enter_author_name_txt_fld.getText(), Integer.parseInt(enter_quantity_txt_fld.getText()),
                        Double.parseDouble(enter_price_txt_fld.getText()), LocalDate.parse(enter_published_date_txt_fld.getText()));
                DBUtlis.executeUpdate("update bookStore\n" +
                        "set bookName = ?, authorName = ?, quantityInStock = ?, leastPrice = ?, publishDate = ?\n" +
                        "where bookId = ?; \n", bookInfo.getBookName(),
                        bookInfo.getAuthorName(), bookInfo.getQuantityInStock(),
                        bookInfo.getLeastPrice(), Date.valueOf(bookInfo.getPublishedDate()), bookInfo.getBookId());
                for (int i = 0; i < bookList.size(); i++) {
                    BookInfo temp = bookList.get(i);
                    if (temp.getBookId() == bookInfo.getBookId()) {
                        assign(temp, bookInfo);
                        break;
                    }
                }
                store_tb.refresh();
                clear();
            }
        });

        clear_btn.setOnAction(actionEvent -> clear());
    }
}
