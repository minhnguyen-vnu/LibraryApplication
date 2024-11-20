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
    public TextField get_customer_name_txt_fld;
    public DatePicker get_borrowed_date;
    public DatePicker get_due_date;
    public ChoiceBox<String> status_choice_box;
    public Button search_btn;
    public Button return_btn;

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
        status_choice_box.setItems(FXCollections.observableArrayList("pending", "Need_to_payment", ""));
    }

    private void onAction() {
        return_btn.setOnAction(actionEvent -> {
            get_customer_name_txt_fld.clear();
            get_borrowed_date.setValue(null);
            get_due_date.setValue(null);
            status_choice_box.setValue(null);
            store_tb.setItems(bookList);
            Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View");
        });
        search_btn.setOnAction(actionEvent -> search());
    }

    private void search() {
        String customerName = get_customer_name_txt_fld.getText();
        LocalDate borrowedDate = get_borrowed_date.getValue();
        LocalDate dueDate = get_due_date.getValue();
        String status = status_choice_box.getValue();

        ObservableList<RequestInfo> filteredList = bookList.stream()
                .filter(request -> (customerName.isEmpty() || request.getUsername().equals(customerName)) &&
                        (borrowedDate == null || request.getPickedDate().equals(borrowedDate)) &&
                        (dueDate == null || request.getReturnDate().equals(dueDate)) &&
                        (status == null || status.isEmpty() || request.getRequestStatus().equals(status)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
    }
}
