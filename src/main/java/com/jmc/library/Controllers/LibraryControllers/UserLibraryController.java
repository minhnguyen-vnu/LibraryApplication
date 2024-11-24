package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;

import com.jmc.library.Controllers.Users.UserInfoOverlay;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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
    public ChoiceBox<String> num_row_shown;

    public AnchorPane matte_screen;
    public AnchorPane user_info_pane;
    public TableColumn<BookInfo, ImageView> book_cover_tb_cl;
    public Button reload_btn;

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
        go_to_setting_btn.setOnAction(actionEvent -> {
            Scene currentScene = go_to_setting_btn.getScene();
            try {
                UserInfoOverlay userInfoOverlay = new UserInfoOverlay(currentScene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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

    private void setAccount_avatar_img() {
        account_avatar_img.setImage(LibraryModel.getInstance().getUser().getAvatar());
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

            Image defaultCover = new Image(getClass().getResource("/IMAGES/UnknownBookCover.png").toExternalForm());

            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() {
                    if (bookInfo.getThumbnail() == null) {
                        return defaultCover;
                    } else {
                        return new Image(bookInfo.getThumbnail(), 50, 75, true, true);
                    }
                }
            };

            ImageView bookCoverImage = new ImageView();
            loadImageTask.setOnSucceeded(e -> bookCoverImage.setImage(loadImageTask.getValue()));
            new Thread(loadImageTask).start();

            bookCoverImage.setFitWidth(50);
            bookCoverImage.setFitHeight(75);
            return new SimpleObjectProperty<>(bookCoverImage);
        });
    }
}