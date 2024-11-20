package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.RequestInfo;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class PendingRequestManagement extends RequestManagement implements Initializable {
    public Button update_btn;
    public Button go_to_dashboard_btn;
    public Button go_to_request_btn;
    public Button manage_btn;
    public Button log_out_btn;
    public Button search_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable();
        onAction();
        addBinding();
        showLibrary();
    }

    private void onAction() {
        go_to_dashboard_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Dashboard View"));
        go_to_request_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Request Management"));
        manage_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View"));
    }

    @Override
    protected void showLibrary() {
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select * from PendingRequest");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    RequestInfo currentBook = new RequestInfo(resultSet.getInt("RequestId"), resultSet.getInt("bookId"),
                            resultSet.getString("username"),
                            resultSet.getDate("requestDate").toLocalDate(), resultSet.getDate("returnDate").toLocalDate(),
                            resultSet.getDouble("cost"), resultSet.getString("requestStatus"));
                    bookList.add(currentBook);
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

    @Override
    protected void ChoiceBoxInitialization() {

    }
}
