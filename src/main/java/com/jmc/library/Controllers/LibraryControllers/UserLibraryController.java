package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
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

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class UserLibraryController extends LibraryController implements Initializable {
    public Button go_to_setting_btn;
    public TableColumn<BookInfo, Button> hire_tb_cl;
    public Button go_to_user_library_btn;
    public Button log_out_btn;
    public Label username_lbl;
    public ImageView account_avatar_img;
    public TableColumn<BookInfo, Boolean> add_to_cart_tb_cl;
    public ChoiceBox num_row_shown;
    public Button go_to_dashboard_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonListener();
        initialAction();
        addBinding();
        onAction();
        showLibrary();
    }

    private void setButtonListener(){
        go_to_user_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });
    }

    private void onAction() {
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
                        try {
                            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                                    "    username,\n" +
                                    "    bookId\n" +
                                    "from userRequest\n" +
                                    "where username = ? and bookId = ?;", LibraryModel.getInstance().getUser().getUsername(), book.getBookId());
                            if (resultSet.next()){
                                System.out.println("You already have this book");
                                return;
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        DBUtlis.executeUpdate("update bookStore\n" +
                                "set quantityInStock = quantityInStock - 1\n" +
                                "where bookId = ?;", book.getBookId());
                        book.setQuantityInStock(book.getQuantityInStock() - 1);
                        LocalDate expiryDate = LocalDate.of(2024, 12, 19);
                        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
                        DBUtlis.executeUpdate("insert into userRequest\n" +
                                "values(?, ?, ?, ?, ?, ?); ", book.getBookId(), book.getBookName(), LibraryModel.getInstance().getUser().getUsername(),
                                LocalDate.now(), expiryDate, book.getLeastPrice() * daysBetween);
                        getTableView().refresh();
                        UserBookInfo userBookInfo = new UserBookInfo(book.getBookName(), book.getAuthorName(),  book.getBookId(), LocalDate.now(), expiryDate, book.getLeastPrice() * daysBetween);
                        addBookforUser(userBookInfo);
                    });
                }
            }
        });
        add_to_cart_tb_cl.setCellFactory(param -> new TableCell<BookInfo, Boolean>() {
            private final CheckBox checkBox = new CheckBox("Cart");

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    BookInfo book = getTableView().getItems().get(getIndex());
                    checkBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        book.setInCart(isNowSelected);
                    });
                    setGraphic(checkBox);
                }
            }
        });
    }

    private void addBookforUser(UserBookInfo addedBook){
        LibraryModel.getInstance().getUser().getBookList().add(addedBook);
    }
}