package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.RequestInfo;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Database.DBUtlis;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.DashboardModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * Controller class for managing the pending requests in the library.
 */
public class PendingRequestManagement extends RequestManagement implements Initializable {
    public Button update_btn;
    public Label noiti_lbl;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onAction();
        setTable();
        addBinding();
        ChoiceBoxInitialization();
        addedAction();
        showLibrary();
    }

    @Override
    protected void showLibrary() {
        addLoading();
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select * from PendingRequest");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    RequestInfo currentBook = new RequestInfo(resultSet.getInt("RequestId"), resultSet.getInt("bookId"),
                            resultSet.getString("bookName"), resultSet.getString("username"),
                            resultSet.getDate("requestDate").toLocalDate(), resultSet.getDate("returnDate").toLocalDate(),
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
     * Adds the action to be executed when the button is clicked.
     */
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

    /**
     * Adds the action to be executed when the button is clicked.
     */
    private void addedAction() {
        update_btn.setOnAction(actionEvent -> {
            RequestInfo foundElement = getElement();
            if (foundElement != null && status_choice_box.getValue() != null) {
                boolean isOK = true;
                for (BookInfo bookInfo : AdminLibraryModel.getInstance().getBookList()) {
                    if (foundElement.getBookId() == bookInfo.getBookId()) {
                        if (bookInfo.getQuantityInStock() == 0) {
                            isOK = false;
                            break;
                        }
                    }
                }
                if (isOK) {
                    System.out.println("Order Accepted");
                    String new_status = status_choice_box.getValue();
                    if (!new_status.equals(foundElement.getRequestStatus()) && !new_status.isEmpty()) {
                        if (new_status.equals("Accepted")) {
                            System.out.println(foundElement);
                            for (BookInfo bookInfo : AdminLibraryModel.getInstance().getBookList()) {
                                if (foundElement.getBookId() == bookInfo.getBookId()) {
                                    bookInfo.setQuantityInStock(bookInfo.getQuantityInStock() - 1);
                                    break;
                                }
                            }

                            DBUpdate dbUpdate = new DBUpdate("insert into userRequest(bookId, bookName, username, pickedDate, returnDate, cost, requestStatus)\n" +
                                    "values(?, ?, ?, ?, ?, ?, ?); ", foundElement.getBookId(), foundElement.getBookName(), foundElement.getUsername(),
                                    foundElement.getPickedDate(), foundElement.getReturnDate(), foundElement.getTotalCost(), "Borrowing");
                            Thread thread = new Thread(dbUpdate);
                            thread.setDaemon(true);
                            thread.start();

                            DBUpdate dbUpdate1 = new DBUpdate("update bookStore\n" +
                                    "set quantityInStock = quantityInStock - 1\n" +
                                    "where bookId = ?;", foundElement.getBookId());
                            Thread thread1 = new Thread(dbUpdate1);
                            thread1.setDaemon(true);
                            thread1.start();
                        }
                        DBUpdate dbUpdate2 = new DBUpdate("delete from PendingRequest\n" +
                                "where RequestId = ?;", foundElement.getIssueId());
                        Thread thread2 = new Thread(dbUpdate2);
                        thread2.setDaemon(true);
                        thread2.start();
                        bookList.remove(foundElement);
                        store_tb.setItems(bookList);
                    }
                }
            }
        });

    }

    @Override
    protected void ChoiceBoxInitialization() {
        status_choice_box.setItems(FXCollections.observableArrayList("Pending", "Accepted", "Rejected", ""));
    }
}
