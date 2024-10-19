package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLibraryController extends LibraryController implements Initializable {
    public Button settings_btn;
    public ImageView account_avatar_img;
    public Label account_name_lbl;
    public Button log_out_btn;
    public Button go_to_setting_btn;
    public Button manage_btn;
    public Button go_to_library_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialAction();
        setTable();
        addBinding();
        onAction();
        showLibrary();
    }

    @Override
    protected void setTable() {
        bookList = AdminLibraryModel.getInstance().getBookList();
        store_tb.setItems(bookList);
    }

    public void onAction() {
        settings_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Managemental Book");
        });
    }
}
