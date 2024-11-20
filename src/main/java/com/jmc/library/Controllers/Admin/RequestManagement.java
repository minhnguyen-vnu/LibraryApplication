package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.RequestInfo;
import com.jmc.library.Database.DBQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public abstract class RequestManagement {
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

    protected void setTable() {
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);
    }

    protected void addBinding() {
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
}
