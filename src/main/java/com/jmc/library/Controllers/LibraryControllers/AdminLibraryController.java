package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLibraryController extends LibraryController implements Initializable {
    private static final Log log = LogFactory.getLog(AdminLibraryController.class);
    public Button settings_btn;
    public ImageView account_avatar_img;
    public Label account_name_lbl;
    public Button log_out_btn;
    public Button go_to_request_btn;
    public Button manage_btn;
    public Button go_to_dashboard_btn;
    public Button go_to_pending_btn;
    public Button reload_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialAction();
        setTable();
        addBinding();
        onAction();
        addTableClickListener();
        showLibrary();
    }

    @Override
    protected void setTable() {
        bookList = AdminLibraryModel.getInstance().getBookList();
        store_tb.setItems(bookList);
    }

    public void onAction() {
        settings_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Managemental Book"));
        go_to_dashboard_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Dashboard View"));
        go_to_request_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Request Management"));
        go_to_pending_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Pending Request Management"));
        log_out_btn.setOnAction(actionEvent -> stageTransforming());
    }

    private void stageTransforming() {
        Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().showAuthenticationWindow();
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Login");
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("");
    }
}
