package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;

import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Controllers.Users.User;
import com.jmc.library.Controllers.Users.UserInfoOverlay;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.BookModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.LightBase;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for managing the user's library view, including displaying and updating the list of books.
 */
public class UserLibraryController extends LibraryController implements Initializable {
    public Button go_to_setting_btn;
    public Button go_to_user_library_btn;
    public Button back_to_dashboard_btn;
    public Button go_to_store_btn;
    public Button cart_btn;
    public Button pending_btn;
    public Button log_out_btn;

    public Label username_lbl;

    public ImageView account_avatar_img;
    public ChoiceBox<String> num_row_shown;

    public AnchorPane matte_screen;
    public AnchorPane user_info_pane;

    /**
     * Initializes the controller and sets up the initial state.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        initialAction();
        setButtonListener();
        setMaterialListener();
        setTable();
        showLibrary();
    }

    /**
     * Adds bindings to the UI components.
     */
    @Override
    protected void addBinding() {
        super.addBinding();
        book_cover_tb_cl.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        account_avatar_img.imageProperty().bind(LibraryModel.getInstance().getUser().avatarProperty());
        Circle clip = new Circle(account_avatar_img.getFitWidth()/2, account_avatar_img.getFitHeight()/2, Math.min(account_avatar_img.getFitHeight()/2, account_avatar_img.getFitWidth()/2));
        account_avatar_img.setClip(clip);
    }

    @Override
    protected void setTable() {
        bookList = LibraryModel.getInstance().getBookList();
    }

    /**
     * Sets up the button listeners for various actions.
     */
    @Override
    protected void showLibrary() {
        addLoading();
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select * from bookStore\n" +
                "where quantityInStock > 0;");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    Blob blob = resultSet.getBlob("imageView");
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    Image image = ImageUtils.byteArrayToImage(imageBytes);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(75);
                    imageView.setFitWidth(50);
                    BookInfo currentBook = new BookInfo(resultSet.getInt(
                            "bookId"),
                            resultSet.getString("bookName"),
                            resultSet.getString("authorName"),
                            resultSet.getInt("quantityInStock"),
                            resultSet.getDouble("leastPrice"),
                            resultSet.getDate("publishDate").toLocalDate(),
                            resultSet.getString("ISBN"),
                            resultSet.getString("publisher"),
                            resultSet.getString("genre"),
                            resultSet.getString("originalLanguage"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail"),
                            imageView,
                            resultSet.getDouble("rate"),
                            resultSet.getInt("rateQuantities"));
                    bookList.add(currentBook);
                }
                resultSet.close();
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
            returnLoading();
        });
        dbQuery.setOnFailed(event -> {
            System.out.println("Failed");
            returnLoading();
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    private void setButtonListener() {
        go_to_user_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });
        back_to_dashboard_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Dashboard");
        });
        go_to_store_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });
        log_out_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().resetAll();
            Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
            Model.getInstance().getViewFactory().showAuthenticationWindow();
            DBUpdate dbUpdate = new DBUpdate("update users\n" +
                    "set status = ?\n" +
                    "where username = ?;", "offline", LibraryModel.getInstance().getUser().getUsername());
            Thread thread = new Thread(dbUpdate);
            thread.setDaemon(true);
            thread.start();
            LibraryModel.getInstance().getUser().resetAll();
        });
        cart_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Cart");
        });
        pending_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });
        go_to_setting_btn.setOnAction(actionEvent -> {
            Scene currentScene = go_to_setting_btn.getScene();
            try {
                UserInfoOverlay userInfoOverlay = new UserInfoOverlay(currentScene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        store_tb.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                BookInfo book = store_tb.getSelectionModel().getSelectedItem();
                BookModel.getInstance().setBookInfo(book);
                Model.getInstance().getViewFactory().getSelectedUserMode().set("Book Detail");
            }
        });
    }

    /**
     * Sets up listeners for various UI components.
     */
    private void setMaterialListener() {
        setUsername_lbl();
        setNum_row_shown();
        LibraryModel.getInstance().getUser().nameProperty().addListener(((observableValue, s, t1) -> {
            username_lbl.setText(t1);
        }));
    }

    /**
     * Sets the username label with the current user's name.
     */
    private void setUsername_lbl() {
        username_lbl.setText(LibraryModel.getInstance().getUser().getName());
    }

    /**
     * Sets up the choice box for selecting the number of rows to be shown in the table.
     */
    private void setNum_row_shown() {
        num_row_shown.getItems().addAll("5", "10", "15", "20", "All");
        num_row_shown.setValue("All");
        num_row_shown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            store_tb.refresh();
            if(newVal.equals("All")) {
                store_tb.setItems(FXCollections.observableArrayList(bookList));
            } else {
                store_tb.setItems(FXCollections.observableArrayList(bookList.stream().limit(Integer.parseInt(newVal)).collect(Collectors.toList())));
            }
        });
    }
}