package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Interface.InterfaceManager;
import com.jmc.library.Controllers.Users.CartEntityController;
import com.jmc.library.Controllers.Interface.CartUpdateListener;
import com.jmc.library.DBUtlis;
import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
    public Button back_to_dashboard_btn;
    public Button cart_btn;
    public Button pending_btn;
    public AnchorPane matte_screen;
    public AnchorPane user_info_pane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        onAction();
        initialAction();
        setButtonListener();
        setMaterialListener();
        showLibrary();
    }

    private void setButtonListener() {
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
            Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
            Model.getInstance().getViewFactory().showAuthenticationWindow();
        });
        back_to_dashboard_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Dashboard");
        });
        cart_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Cart");
        });
        pending_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });
    }

    private void setMaterialListener(){
        try {
            ResultSet resultSet = DBUtlis.executeQuery("SELECT * \n" +
                    "FROM bookStore b\n" +
                    "WHERE b.bookId NOT IN (\n" +
                    "    SELECT r.bookId\n" +
                    "    FROM userRequest r\n" +
                    "    WHERE r.status = 'accepted'\n" +
                    ");");

            if (resultSet.next()) {
                while (resultSet.next()) {
                    BookInfo book = new BookInfo(resultSet.getInt("bookId"), resultSet.getString("bookName"),
                            resultSet.getDate("publishDate").toLocalDate(), resultSet.getString("authorName"),
                            resultSet.getInt("quantityInStock"), resultSet.getDouble("leastPrice"),
                            resultSet.getString("ISBN"));
                    bookList.add(book);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());

        num_row_shown.getItems().addAll(5, 10, 15, 20);
        num_row_shown.setValue(5);
        num_row_shown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            store_tb.refresh();
            store_tb.setItems(FXCollections.observableArrayList(bookList.stream().limit(newVal).collect(Collectors.toList())));
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
                        if (!wasSelected && isNowSelected) {
                            try {
                                ResultSet resultSet = getResultSetCaseSelectBook(book);
                                if (resultSet.next()) {
                                    System.out.println("You already have this book");
                                    return;
                                }
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            updDatabaseCaseInsertBook(book);

                            getTableView().refresh();
                            checkBox.setSelected(true);
                        } else if(wasSelected && !isNowSelected) {
                            checkBox.setSelected(true);
                        }
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
                        System.out.println("You clicked on " + BookModel.getInstance().getBookInfo().getBookName());
                        Model.getInstance().getViewFactory().getSelectedUserMode().set("Book Detail");
                        Model.getInstance().getViewFactory().getBookDisplayController().setDisplayBook(book);
                    });
                }
            }
        });
    }

    private void addBookForUser(UserBookInfo addedBook){
        LibraryModel.getInstance().getUser()
                .getBookWaitingPaymentList().add(addedBook);
        LibraryModel.getInstance().getUser()
                .getCartEntityControllers().add(new CartEntityController(addedBook));
        InterfaceManager.getInstance()
                .getCartUpdateListener()
                .onAddCartEntity(new CartEntityController(addedBook));
    }

    public void updDatabaseCaseInsertBook (BookInfo book) {
        DBUtlis.executeUpdate("update bookStore\n" +
                "set quantityInStock = quantityInStock - 1\n" +
                "where bookId = ?;", book.getBookId());
        book.setQuantityInStock(book.getQuantityInStock() - 1);
        DBUtlis.executeUpdate("insert into userRequest\n" +
                        "values(?, ?, ?, ?, ?, ?, ?); ", book.getBookId(), book.getBookName(),
                LibraryModel.getInstance().getUser().getUsername(),
                LocalDate.now(), LocalDate.now(), book.getLeastPrice(),
                "Need_to_payment");
        UserBookInfo userBookInfo = new UserBookInfo(book.getBookName(), book.getAuthorName(),
                book.getBookId(), LocalDate.now(),
                LocalDate.now(), book.getLeastPrice(),
                "Need_to_payment");
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