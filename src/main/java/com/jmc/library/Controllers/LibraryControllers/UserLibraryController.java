package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Interface.InterfaceManager;
import com.jmc.library.Controllers.Users.CartEntityController;
import com.jmc.library.DataBase.*;
import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
    public Button go_to_user_library_btn;
    public Button go_to_dashboard_btn;
    public Button go_to_store_btn;
    public Button cart_btn;
    public Button pending_btn;
    public Button log_out_btn;
    
    public Label username_lbl;
    public ImageView account_avatar_img;
    public TableColumn<BookInfo, Boolean> add_to_cart_tb_cl;
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public ChoiceBox<Integer> num_row_shown;

    public AnchorPane matte_screen;
    public AnchorPane user_info_pane;
    public TableColumn<BookInfo, ImageView> book_cover_tb_cl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        onAction();
        initialAction();
        setButtonListener();
        setMaterialListener();
        setTable();
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
            LibraryModel.getInstance().getUser().resetAll();
            Model.getInstance().getViewFactory().resetAll();
            Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
            Model.getInstance().getViewFactory().showAuthenticationWindow();
        });
        cart_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Cart");
        });
        pending_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });
    }

    private void setMaterialListener() {
        setUsername_lbl();
        setNum_row_shown();
        setAccount_avatar_img();
    }

    private void setUsername_lbl() {
        username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
    }

    private void setNum_row_shown() {
        num_row_shown.getItems().addAll(5, 10, 15, 20);
        num_row_shown.setValue(5);
        num_row_shown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            store_tb.refresh();
            store_tb.setItems(FXCollections.observableArrayList(bookList.stream().limit(newVal).collect(Collectors.toList())));
        });
    }

    private void setAccount_avatar_img() {
        account_avatar_img.setImage(new ImageView(getClass().getResource("/IMAGES/avatar.png")
                .toExternalForm()).getImage());

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
        setBook_cover_tb_cl();
    }

    private void setBook_cover_tb_cl() {
        book_cover_tb_cl.setCellFactory(col -> new TableCell<BookInfo, ImageView>() {
            @Override
            protected void updateItem(ImageView imageView, boolean empty) {
                super.updateItem(imageView, empty);
                if (empty || imageView == null) {
                    setGraphic(null);
                } else {
                    setGraphic(imageView);
                }
            }
        });

        book_cover_tb_cl.setCellValueFactory(param -> {
            BookInfo bookInfo = param.getValue();

            // Ảnh mặc định chỉ cần load một lần
            Image defaultCover = new Image(getClass().getResource("/IMAGES/UnknownBookCover.png").toExternalForm());

            // Sử dụng Task để tải ảnh nền
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    if (bookInfo.getThumbnail() == null) {
                        return defaultCover;
                    } else {
                        return new Image(bookInfo.getThumbnail(), 50, 75, true, true);  // Tải với kích thước tối ưu
                    }
                }
            };

            // Tạo ImageView và cập nhật sau khi ảnh được tải xong
            ImageView bookCoverImage = new ImageView();
            loadImageTask.setOnSucceeded(e -> bookCoverImage.setImage(loadImageTask.getValue()));
            new Thread(loadImageTask).start();  // Chạy Task trong luồng nền

            bookCoverImage.setFitWidth(50);
            bookCoverImage.setFitHeight(75);
            return new SimpleObjectProperty<>(bookCoverImage);
        });
    }

    private void addBookForUser(UserBookInfo addedBook){
        LibraryModel.getInstance().getUser()
                .getCartEntityControllers().add(new CartEntityController(addedBook));
        LibraryModel.getInstance().getUser()
                        .getBookPendingList().add(addedBook);
        InterfaceManager.getInstance()
                .getCartUpdateListener()
                .onAddCartEntity(new CartEntityController(addedBook));
    }

    public void updDatabaseCaseInsertBook (BookInfo book) {
        DBUpdate dbUpdate = new DBUpdate("update bookStore\n" +
                "set quantityInStock = quantityInStock - 1\n" +
                "where bookId = ?;", book.getBookId());
        Thread thread = new Thread(dbUpdate);
        thread.start();
        book.setQuantityInStock(book.getQuantityInStock() - 1);
        UserBookInfo userBookInfo = new UserBookInfo(book.getBookName(), book.getAuthorName(),
                book.getBookId(), LocalDate.now(),
                LocalDate.now(), book.getLeastPrice(),
                "Need_to_payment");
        DBQuery dbQuery = new DBQuery("select max(issueId) from userRequest;");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                if (resultSet.next()) {
                    DBUtlis.executeUpdate("insert into userRequest\n" +
                                    "values(?, ?, ?, ?, ?, ?, ?, ?); ",
                            resultSet.getInt(1) + 1,
                            book.getBookId(), book.getBookName(),
                            LibraryModel.getInstance().getUser().getUsername(),
                            LocalDate.now(), LocalDate.now(),
                            book.getLeastPrice(),
                            "Need_to_payment");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            addBookForUser(userBookInfo);
        });
        Thread thread1 = new Thread(dbQuery);
        thread1.start();

    }

    public DBQuery getResultSetCaseSelectBook(BookInfo book)  {
        DBQuery dbQuery = new DBQuery("select\n" +
                "    username,\n" +
                "    bookId\n" +
                "from userRequest\n" +
                "where username = ? and bookId = ?;", LibraryModel.getInstance().getUser().getUsername(), book.getBookId());
        return dbQuery;
    }
}