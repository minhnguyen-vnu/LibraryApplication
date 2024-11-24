package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.RequestInfo;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ApprovedRequestManagement extends RequestManagement implements Initializable {
    public Button reload_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxInitialization();
        setTable();
        onAction();
        addBinding();
        showLibrary();
    }

    @Override
    protected void ChoiceBoxInitialization() {
        status_choice_box.setItems(FXCollections.observableArrayList("Pending", "Need_to_payment", ""));
    }

}
