package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Users.User;
import com.jmc.library.DBUtlis;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.lang.constant.ModuleDesc;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class LibraryController implements Initializable {
    public Button go_to_setting_btn;
    public DatePicker clock;
    public TextField search_fld;
    public ImageView search_bar_btn;
    public TableColumn<BookInfo, Integer> book_id_tb_cl;
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public TableColumn<BookInfo, String> author_tb_cl;
    public TableColumn<BookInfo, Integer> quantity_tb_cl;
    public TableColumn<BookInfo, Double> least_price_tb_cl;
    public TableColumn<BookInfo, Button> hire_tb_cl;
    public TableColumn<BookInfo, LocalDate> published_date_tb_cl;
    public TableView<BookInfo> store_tb;
    public ObservableList<BookInfo> bookList;
    public Button search_btn;
    public Button go_to_user_library_btn;
    private String username, usertoken;
    private ObservableList<UserBookInfo> userList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonListener();
        addBinding();
        formatting();
        showLibrary();
        addBookListListener();
    }

    private void setButtonListener(){
        search_btn.setOnAction(actionEvent -> searchBook(search_fld.getText()));
        go_to_user_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });
    }

    private void addBinding() {
        bookList = FXCollections.observableArrayList();
        userList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);

        book_id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        quantity_tb_cl.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        least_price_tb_cl.setCellValueFactory(new PropertyValueFactory<>("leastPrice"));
        published_date_tb_cl.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
    }

    private void formatting() {
        hire_tb_cl.setCellFactory(param -> new TableCell<>() {
            private final Button hireButton = new Button("Hire");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hireButton);
                    hireButton.setOnAction(event -> {
                        BookInfo book = getTableView().getItems().get(getIndex());
                        DBUtlis.executeUpdate("update bookStore\n" +
                                "set quantityInStock = quantityInStock - 1\n" +
                                "where bookId = ?;", book.getBookId());
                        book.setQuantityInStock(book.getQuantityInStock() - 1);
                        getTableView().refresh();
                    });
                }
            }
        });
    }

    private void addBookListListener() {
        bookList.addListener((ListChangeListener<BookInfo>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    store_tb.refresh();
                }
            }
        });
    }

    private void showLibrary() {
        bookList.clear();
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select * from bookStore");
            while (resultSet.next()) {
                BookInfo currentBook = new BookInfo(resultSet.getInt("bookId"), resultSet.getString("bookName"),
                        resultSet.getString("authorName"), resultSet.getInt("quantityInStock"), resultSet.getInt("leastPrice"),
                        resultSet.getDate("publishDate").toLocalDate());
                bookList.add(currentBook);
            }
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private void searchBook(String text) {
        if (text.isEmpty()) {
            System.out.println(1);
            showLibrary();
        } else {
            if (text.charAt(0) != '#') searchBookByName(text);
            else searchBookById(text.substring(1));
        }

    }

    private void searchBookByName(String text) {
        ObservableList<BookInfo> filteredList = bookList.stream()
                .filter(book -> book.getBookName().equalsIgnoreCase(text))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
        store_tb.getItems().addListener((ListChangeListener<BookInfo>) change -> {
            if (!change.next() || change.wasAdded() || change.wasRemoved()) {
                filteredList.clear();
            }
        });
    }

    private void searchBookById(String text) {
        if(text.contains(" ")){
            return;
        }

        int bookId = Integer.parseInt(text);
        ObservableList<BookInfo> filteredList = bookList.stream()
                    .filter(book -> book.getBookId() == bookId)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
        store_tb.getItems().addListener((ListChangeListener<BookInfo>) change -> {
            if (!change.next() || change.wasAdded() || change.wasRemoved()) {
                filteredList.clear();
            }
        });
    }

    public void receiveRequest(String username, String usertoken, ObservableList<UserBookInfo> userList) {
        this.username = username;
        this.usertoken = usertoken;
        this.userList = userList;
    }
}