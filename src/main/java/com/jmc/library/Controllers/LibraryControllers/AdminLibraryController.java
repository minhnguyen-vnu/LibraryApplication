package com.jmc.library.Controllers.LibraryControllers;

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
    public Button back_to_dashboard_btn;
    public Button go_to_dashboard_btn;
    public ChoiceBox num_row_shown;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialAction();
        addBinding();
        showLibrary();
    }
}
