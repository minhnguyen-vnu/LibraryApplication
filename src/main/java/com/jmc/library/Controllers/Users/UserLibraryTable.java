package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.stream.Collectors;

public abstract class UserLibraryTable {
    public TableColumn<UserBookInfo, String> book_name_tb_cl;
    public TableColumn<UserBookInfo, String> author_tb_cl;
    public TableColumn<UserBookInfo, Integer> book_id_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> return_day_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> picked_day_tb_cl;
    public TableView<UserBookInfo> store_tb;
    protected ObservableList<UserBookInfo> bookList;

    public Button go_to_store_btn;
    public Button go_to_library_btn;
    public Button back_to_dashboard_btn;

    public ImageView account_avatar_img;

    public TextField search_fld;
    public Button search_btn;
    public Button log_out_btn;
    public Button cart_btn;
    public Button pending_btn;
    public Button go_to_setting_btn;
    public Button reload_btn;

    public ChoiceBox<String> num_row_shown;
    public Label username_lbl;

    protected void addBinding() {
        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        book_id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        picked_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("pickedDate"));
        return_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        store_tb.setItems(bookList);
    }

    protected void addLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Loading.fxml"));
        try {
            ImageView loading_img = loader.load();
            store_tb.setPlaceholder(loading_img);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void returnLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/NoDataPlaceHolder.fxml"));
        try {
            Label label = loader.load();
            store_tb.setPlaceholder(label);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected void setTable() {
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);
    }

    protected abstract void showLibrary();

    protected void searchBookByAuthor(String authorName) {
        ObservableList<UserBookInfo> filteredList = bookList.stream()
                .filter(book -> book.getAuthorName().equalsIgnoreCase(authorName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
        filteredList.clear();
    }

    protected void setUsername_lbl() {
        username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
    }

    protected void setAccount_avatar_img() {
        account_avatar_img = new ImageView(LibraryModel.getInstance().getUser().getAvatar());
    }

    protected void setNum_row_shown() {
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

    protected void setListenerMaterial() {
        setUsername_lbl();
        setNum_row_shown();
        setAccount_avatar_img();
    }

    protected void setButtonListener() {
        search_btn.setOnAction(actionEvent -> searchBookByAuthor(search_fld.getText()));
        go_to_store_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });
        go_to_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });
        log_out_btn.setOnAction(actionEvent -> {
            LibraryModel.getInstance().getUser().resetAll();
            Model.getInstance().getViewFactory().resetAll();
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
        go_to_setting_btn.setOnAction(actionEvent -> {
            Scene currentScene = go_to_setting_btn.getScene();
            try {
                UserInfoOverlay userInfoOverlay = new UserInfoOverlay(currentScene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
