package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.RequestInfo;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.stream.Collectors;

/**
 * Abstract class for managing the requests in the library.
 */
public abstract class RequestManagement {
    public TextField get_customer_name_txt_fld;
    public DatePicker get_borrowed_date;
    public DatePicker get_due_date;
    public ChoiceBox<String> status_choice_box;
    public TextField get_request_id_txt_fld;
    public Button search_btn;
    public Button return_btn;
    public Button reload_btn;
    public Label noti_lbl;

    public TableView<RequestInfo> store_tb;
    public ObservableList<RequestInfo> bookList;

    public TableColumn<RequestInfo, Integer> issue_id_cl;
    public TableColumn<RequestInfo, Integer> book_id_cl;
    public TableColumn<RequestInfo, String> customer_cl;
    public TableColumn<RequestInfo, LocalDate> borrowed_date_cl;
    public TableColumn<RequestInfo, LocalDate> due_date_cl;
    public TableColumn<RequestInfo, Double> cost_cl;
    public TableColumn<RequestInfo, String> status_cl;


    protected abstract void ChoiceBoxInitialization();

    /**
     * Adds the loading placeholder to the table.
     */
    protected void addLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Loading.fxml"));
        try {
            ImageView loading_img = loader.load();
            store_tb.setPlaceholder(loading_img);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the no data placeholder to the table.
     */
    protected void returnLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/NoDataPlaceHolder.fxml"));
        try {
            Label label = loader.load();
            store_tb.setPlaceholder(label);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up the table with the list of books.
     */
    protected void setTable() {
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);
    }

    /**
     * Adds bindings to the UI components.
     */
    protected void addBinding() {
        issue_id_cl.setCellValueFactory(new PropertyValueFactory<>("issueId"));
        book_id_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        customer_cl.setCellValueFactory(new PropertyValueFactory<>("username"));
        borrowed_date_cl.setCellValueFactory(new PropertyValueFactory<>("pickedDate"));
        due_date_cl.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        cost_cl.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        status_cl.setCellValueFactory(new PropertyValueFactory<>("requestStatus"));
    }

    /**
     * Shows the library of books.
     */
    protected void showLibrary() {
        addLoading();
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select * from userRequest");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    RequestInfo currentBook = new RequestInfo(resultSet.getInt("issueId"), resultSet.getInt("bookId"),
                            resultSet.getString("bookName"), resultSet.getString("username"),
                            resultSet.getDate("pickedDate").toLocalDate(), resultSet.getDate("returnDate").toLocalDate(),
                            resultSet.getDouble("cost"), resultSet.getString("requestStatus"));
                    bookList.add(currentBook);
                }
                resultSet.close();
                returnLoading();
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

    /**
     * Searches for a book in the library.
     */
    protected void search() {
        String issueID = get_request_id_txt_fld.getText();
        String customerName = get_customer_name_txt_fld.getText();
        LocalDate borrowedDate = get_borrowed_date.getValue();
        LocalDate dueDate = get_due_date.getValue();
        String status = status_choice_box.getValue();

        ObservableList<RequestInfo> filteredList = bookList.stream()
                .filter(request -> (issueID.isEmpty() || request.getIssueId() == Integer.parseInt(issueID)) &&
                        (customerName.isEmpty() || request.getUsername().equals(customerName)) &&
                        (borrowedDate == null || request.getPickedDate().equals(borrowedDate)) &&
                        (dueDate == null || request.getReturnDate().equals(dueDate)) &&
                        (status == null || status.isEmpty() || request.getRequestStatus().equals(status)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);

        get_request_id_txt_fld.clear();
        get_customer_name_txt_fld.clear();
        get_borrowed_date.setValue(null);
        get_due_date.setValue(null);
        status_choice_box.setValue("");
    }

    /**
     * Initializes the controller and sets up the initial state.
     */
    protected void onAction() {
        return_btn.setOnAction(actionEvent -> {
            get_customer_name_txt_fld.clear();
            get_borrowed_date.setValue(null);
            get_due_date.setValue(null);
            get_request_id_txt_fld.clear();
            status_choice_box.setValue(null);
            store_tb.setItems(bookList);
            noti_lbl.setText("");
            Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View");
        });
        search_btn.setOnAction(actionEvent -> search());

        store_tb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int requestID = newValue.getIssueId();
                String customer = newValue.getUsername();
                LocalDate requestedDate = newValue.getPickedDate();
                LocalDate dueDate = newValue.getReturnDate();

                get_request_id_txt_fld.setText(String.valueOf(requestID));
                get_customer_name_txt_fld.setText(customer);
                get_borrowed_date.setValue(requestedDate);
                get_due_date.setValue(dueDate);
            }
        });

        reload_btn.setOnAction(actionEvent -> showLibrary());
    }
}
