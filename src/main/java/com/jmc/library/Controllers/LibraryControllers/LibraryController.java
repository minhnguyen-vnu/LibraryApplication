package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Admin.AdminTableItiem;
import com.jmc.library.Controllers.Users.User;
import com.jmc.library.DBUtlis;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.lang.constant.ModuleDesc;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class LibraryController implements Initializable {
    public Button go_to_setting_btn;
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
    public Button log_out_btn;
    public Label username_lbl;
    public ImageView account_avatar_img;
    public TableColumn<BookInfo,CheckBox> add_to_cart_tb_cl;
    /**
     * The number of rows that will be shown in the table
     */
    public ChoiceBox num_row_shown;
    public Button go_to_dashboard_btn;
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
            System.out.println(LibraryModel.getInstance().getUserList());
            System.out.println(System.identityHashCode(LibraryModel.getInstance().getUserList()));
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
        add_to_cart_tb_cl.setCellValueFactory(new PropertyValueFactory<>("add_to_cart"));
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
                        UserBookInfo userBookInfo = new UserBookInfo(book.getBookName(), book.getAuthorName(),  book.getBookId(), LocalDate.now(), LocalDate.of(2025, 10, 1), 100);
                        addBookforUser(userBookInfo);
                    });
                }
            }
        });
        add_to_cart_tb_cl.setCellFactory(param->new TableCell<>(){
            private CheckBox checkBox = new CheckBox();
            @Override
            protected void updateItem(CheckBox item, boolean empty) {
                super.updateItem(item, empty);
                if(empty){
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
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
        store_tb.setItems(bookList);
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
        LibraryModel.getInstance().setUser(username, usertoken, userList);
    }

    private void addBookforUser(UserBookInfo addedBook){
        LibraryModel.getInstance().getUserList().add(addedBook);
    }
}