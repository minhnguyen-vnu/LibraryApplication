package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.DBUtlis;
import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import com.mysql.cj.xdevapi.CreateIndexParams;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public ChoiceBox<Integer> num_row_shown;
    public Button go_to_dashboard_btn;
    public Button go_to_store_btn;
    public AnchorPane matte_screen;
    public AnchorPane user_info_pane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonListener();
        setMaterialListener();
        initialAction();
        addBinding();
        onAction();
        showLibrary();
    }

    private void setButtonListener(){
        go_to_user_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });
        go_to_dashboard_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Dashboard");
        });
        go_to_store_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });
        log_out_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().showAuthenticationWindow();
            Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
        });
    }

    private void setMaterialListener() {

        username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());

        num_row_shown.getItems().addAll(5, 10, 15, 20);
        num_row_shown.setValue(5);
        num_row_shown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            store_tb.setItems(FXCollections.observableArrayList(bookList.stream().limit(newVal).collect(Collectors.toList())));
        });

        bookList.addListener((ListChangeListener<BookInfo>) change -> {
            store_tb.setItems(FXCollections.observableArrayList(bookList.stream().limit(num_row_shown.getValue()).collect(Collectors.toList())));
        });

        account_avatar_img.setOnMouseClicked(mouseEvent -> {
            if(user_info_pane.getChildren().isEmpty()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UserInfo.fxml"));
                try {
                    user_info_pane.getChildren().add(loader.load());
                    matte_screen.setVisible(true);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                user_info_pane.getChildren().clear();
                matte_screen.setVisible(false);
            }
        });
        matte_screen.setOnMouseClicked(mouseEvent -> {
            if(!user_info_pane.getChildren().isEmpty()) {
                user_info_pane.getChildren().clear();
                matte_screen.setVisible(false);
            }
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
                            ResultSet resultSet= getResultSetCaseSelectBook(book);
                            if (resultSet.next()) {
                                System.out.println("You already have this book");
                                return;
                            }
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        updDatabaseCaseInsertBook(book);
                        getTableView().refresh();
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

        book_name_tb_cl.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    BookInfo book = getTableView().getItems().get(getIndex());
                    setText(book.getBookName());
                    setOnMouseClicked(event -> {
                        BookModel.getInstance().setBookInfo(book);
                        //System.out.println("Book name: " + book.getBookName());
                        Stage currentStage = (Stage) go_to_user_library_btn.getScene().getWindow();
                        Model.getInstance().getViewFactory().closeStage(currentStage);
                        Model.getInstance().getViewFactory().showDisplayBook();
                        //System.out.println("Book name: " + book.getBookName());
                    });
                }
            }
        });
    }

    private void addBookForUser(UserBookInfo addedBook){
        LibraryModel.getInstance().getUser().getBookList().add(addedBook);
    }

    public void updDatabaseCaseInsertBook (BookInfo book) {
        DBUtlis.executeUpdate("update bookStore\n" +
                "set quantityInStock = quantityInStock - 1\n" +
                "where bookId = ?;", book.getBookId());
        book.setQuantityInStock(book.getQuantityInStock() - 1);
        LocalDate expiryDate = LocalDate.of(2024, 12, 19);
        long daysBetween = ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
        DBUtlis.executeUpdate("insert into userRequest\n" +
                        "values(?, ?, ?, ?, ?, ?); ", book.getBookId(), book.getBookName(), LibraryModel.getInstance().getUser().getUsername(),
                LocalDate.now(), expiryDate, book.getLeastPrice() * daysBetween);
        UserBookInfo userBookInfo = new UserBookInfo(book.getBookName(), book.getAuthorName(),  book.getBookId(), LocalDate.now(), expiryDate, book.getLeastPrice() * daysBetween);
        addBookForUser(userBookInfo);
    }

    public ResultSet getResultSetCaseSelectBook(BookInfo book) throws SQLException {
        return DBUtlis.executeQuery("select\n" +
                "    username,\n" +
                "    bookId\n" +
                "from userRequest\n" +
                "where username = ? and bookId = ?;", LibraryModel.getInstance().getUser().getUsername(), book.getBookId());
    }
}