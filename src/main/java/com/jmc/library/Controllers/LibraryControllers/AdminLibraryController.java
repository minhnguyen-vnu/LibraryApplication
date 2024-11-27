package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing the admin library view, including displaying and updating the list of books.
 */
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
    public Button user_management_btn;

    /**
     * Initializes the controller and sets up the initial state.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialAction();
        setTable();
        addBinding();
        onAction();
        addTableClickListener();
        showLibrary();
    }

    /**
     * Adds bindings to the UI components.
     */
    @Override
    protected void addBinding() {
        super.addBinding();
        book_cover_tb_cl.setCellValueFactory(new PropertyValueFactory<>("imageView"));
    }

    /**
     * Sets up the table with the list of books.
     */
    @Override
    protected void setTable() {
        bookList = AdminLibraryModel.getInstance().getBookList();
        store_tb.setItems(bookList);
    }

    /**
     * Sets up the button listeners for various actions.
     */
    public void onAction() {
        settings_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Managemental Book"));
        go_to_dashboard_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Dashboard View"));
        go_to_request_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Request Management"));
        go_to_pending_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Pending Request Management"));
        user_management_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin User Management"));
        log_out_btn.setOnAction(actionEvent -> stageTransforming());
    }

    /**
     * Handles the stage transformation when logging out.
     */
    private void stageTransforming() {
        Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().showAuthenticationWindow();
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Login");
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("");
    }
}
