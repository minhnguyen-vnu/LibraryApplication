package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.BookInfo;
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

public class RequestManagement implements Initializable {
    public TextField get_customer_name_txt_fld;
    public DatePicker get_borrowed_date;
    public DatePicker get_due_date;
    public ChoiceBox<String> status_choice_box;
    public Button search_btn;
    public TableColumn<RequestInfo, Integer> issue_id_cl;
    public TableColumn<RequestInfo, Integer> book_id_cl;
    public TableColumn<RequestInfo, String> customer_cl;
    public TableColumn<RequestInfo, LocalDate> borrowed_date_cl;
    public TableColumn<RequestInfo, LocalDate> due_date_cl;
    public TableColumn<RequestInfo, Double> cost_cl;
    public TableColumn<RequestInfo, String> status_cl;
    public TableView<RequestInfo> store_tb;
    public ObservableList<RequestInfo> bookList;
    public Button return_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxInitialization();
        setTable();
        onAction();
        addBinding();
        showLibrary();
    }

    private void ChoiceBoxInitialization() {
        status_choice_box.setItems(FXCollections.observableArrayList("pending", "Need_to_payment", ""));
    }

    private void setTable() {
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);
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

    private void addBinding() {
        issue_id_cl.setCellValueFactory(new PropertyValueFactory<>("issueId"));
        book_id_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        customer_cl.setCellValueFactory(new PropertyValueFactory<>("username"));
        borrowed_date_cl.setCellValueFactory(new PropertyValueFactory<>("pickedDate"));
        due_date_cl.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        cost_cl.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        status_cl.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
    }

    protected void showLibrary() {
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select * from userRequest");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    RequestInfo currentBook = new RequestInfo(resultSet.getInt("issueId"), resultSet.getInt("bookId"),
                            resultSet.getString("username"),
                            resultSet.getDate("pickedDate").toLocalDate(), resultSet.getDate("returnDate").toLocalDate(),
                            resultSet.getDouble("cost"), resultSet.getString("requestStatus"));
                    bookList.add(currentBook);
                    System.out.println(currentBook);
                }
                resultSet.close();
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        });
        dbQuery.setOnFailed(event -> {
            System.out.println("Failed");
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
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
