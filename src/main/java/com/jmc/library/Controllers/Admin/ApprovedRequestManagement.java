package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.RequestInfo;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for managing the approved requests in the library.
 */
public class ApprovedRequestManagement extends RequestManagement implements Initializable {
    public Button update_book_btn;

    public Label noiti_lbl;
    public Button return_book_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ChoiceBoxInitialization();
        setTable();
        onAction();
        addBinding();
        addedAction();
        showLibrary();
    }

    @Override
    protected void ChoiceBoxInitialization() {
        status_choice_box.setItems(FXCollections.observableArrayList("Borrowing", "Returned", ""));
    }

    private RequestInfo getElement() {
        int requestID = Integer.parseInt(get_request_id_txt_fld.getText());
        String customer = get_customer_name_txt_fld.getText();
        LocalDate requestedDate = get_borrowed_date.getValue();
        LocalDate dueDate = get_due_date.getValue();

        if (get_request_id_txt_fld.getText().isEmpty() || customer.isEmpty() || requestedDate == null || dueDate == null) {
            return null;
        }

        for (RequestInfo requestInfo : bookList) {
            if (requestID == requestInfo.getIssueId() && customer.equals(requestInfo.getUsername())
                    && requestedDate.equals(requestInfo.getPickedDate()) && dueDate.equals(requestInfo.getReturnDate())) {
                return requestInfo;
            }
        }

        return null;
    }

    private void addedAction() {
        update_book_btn.setOnAction(actionEvent -> {
            RequestInfo foundElement = getElement();
            if (foundElement != null && status_choice_box.getValue() != null) {
                String new_status = status_choice_box.getValue();
                if (!new_status.equals(foundElement.getRequestStatus()) && new_status.equals("Returned")) {
                    DBUpdate dbUpdate = new DBUpdate("update bookStore\n" +
                            "set quantityInStock = quantityInStock + 1\n" +
                            "where bookId = ?; ", foundElement.getBookId());
                    Thread thread = new Thread(dbUpdate);
                    thread.setDaemon(true);
                    thread.start();

                    DBUpdate dbUpdate1 = new DBUpdate("update userRequest\n" +
                            "set requestStatus = 'Returned'\n" +
                            "where issueId = ?; ", foundElement.getIssueId());
                    Thread thread1 = new Thread(dbUpdate1);
                    thread1.setDaemon(true);
                    thread1.start();

                    foundElement.setRequestStatus("Returned");
                    store_tb.refresh();
                    for (BookInfo bookInfo : AdminLibraryModel.getInstance().getBookList()) {
                        if (foundElement.getBookId() == bookInfo.getBookId()) {
                            bookInfo.setQuantityInStock(bookInfo.getQuantityInStock() + 1);
                            break;
                        }
                    }
                }
            }
            store_tb.setItems(bookList);
            get_request_id_txt_fld.clear();
            get_customer_name_txt_fld.clear();
            get_borrowed_date.setValue(null);
            get_due_date.setValue(null);
            status_choice_box.setValue("");
        });
    }
}
