package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.DataBase.*;
import com.jmc.library.Models.LibraryModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
    private String username;
    private String token;

    private ObservableList<UserBookInfo> bookPendingList;
    private ObservableList<UserBookInfo> bookHiredList;
    private ObservableList<CartEntityController> cartEntityControllers;

    public User() {
        this.bookPendingList = FXCollections.observableArrayList();
        this.bookHiredList = FXCollections.observableArrayList();
        this.cartEntityControllers = FXCollections.observableArrayList();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ObservableList<UserBookInfo> getBookPendingList() {
        return bookPendingList;
    }

    public void setBookPendingList(ObservableList<UserBookInfo> bookList) {
        this.bookPendingList = bookList;
    }

    public ObservableList<UserBookInfo> getBookHiredList() {
        return bookHiredList;
    }

    public void setBookHiredList(ObservableList<UserBookInfo> bookHiredList) {
        this.bookHiredList = bookHiredList;
    }

    public ObservableList<CartEntityController> getCartEntityControllers() {
        return cartEntityControllers;
    }

    public void setCartEntityControllers(ObservableList<CartEntityController> cartEntityControllers) {
        this.cartEntityControllers = cartEntityControllers;
    }

    public void loadAllList() {
        loadBookHiredList();
        loadBookPendingList();
    }

    public void loadBookPendingList() {
        bookPendingList.clear();
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                    "    r.username,\n" +
                    "    r.bookName,\n" +
                    "    b.authorName,\n" +
                    "    r.bookId,\n" +
                    "    r.requestDate,\n" +
                    "    r.returnDate,\n" +
                    "    r.cost,\n" +
                    "    r.requestStatus\n"+
                    "from PendingRequest r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ? order by r.requestStatus;", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("requestDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"), resultSet.getString("requestStatus"));
                this.bookPendingList.add(userBookInfo);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBookHiredList() {
        bookHiredList.clear();
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                    "    r.username,\n" +
                    "    r.bookName,\n" +
                    "    b.authorName,\n" +
                    "    r.bookId,\n" +
                    "    r.pickedDate,\n" +
                    "    r.returnDate,\n" +
                    "    r.cost,\n" +
                    "    r.requestStatus\n"+
                    "from  userRequest r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ? " +
                    "order by r.requestStatus;", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"),
                        resultSet.getString("requestStatus"));
                this.bookHiredList.add(userBookInfo);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeCartEntityController(CartEntityController cartEntityController) {
        this.cartEntityControllers.remove(cartEntityController);
    }

    public String getSubTotal() {
        double total = this.cartEntityControllers.stream().mapToDouble(cartEntityController -> cartEntityController.getUserBookInfo().getTotalCost()).sum();
        return Double.toString(total);

    }

    public String getAdditional() {
        return "0.00";
    }

    public String getTotal() {
        return String.format("%.2f",Double.parseDouble(getSubTotal()) + Double.parseDouble(getAdditional()));
    }

    public void userPayment() {
        for(CartEntityController cartEntityController : this.cartEntityControllers) {
            LibraryModel.getInstance().getUser().getBookPendingList()
                    .add(cartEntityController.getUserBookInfo());
            DBQuery dbQuery = new DBQuery("select max(requestId) from PendingRequest");
            AtomicInteger requestId = new AtomicInteger();
            dbQuery.setOnSucceeded(event -> {
                try {
                    ResultSet resultSet = dbQuery.getValue();
                    if (resultSet.next()) {
                       requestId.set(resultSet.getInt(1));
                    }
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                DBUpdate dbUpdate = new DBUpdate("INSERT INTO PendingRequest(requestId, bookId, bookName, username, requestDate, returnDate, cost, requestStatus) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?)",
                        requestId.get(),
                        cartEntityController.getUserBookInfo().getBookId(),
                        cartEntityController.getUserBookInfo().getBookName(),
                        this.username,
                        cartEntityController.getUserBookInfo().getPickedDate(),
                        cartEntityController.getUserBookInfo().getReturnDate(),
                        cartEntityController.getUserBookInfo().getTotalCost(),
                        cartEntityController.getUserBookInfo().getRequestStatus()
                        );

                Thread thread = new Thread(dbUpdate);
                thread.start();
            });
            Thread thread = new Thread(dbQuery);
            thread.start();
        }
    }

    public void clearCart() {
        this.cartEntityControllers.clear();
    }

    public void resetAll() {
        System.out.println("User.resetAll");
        this.bookPendingList.clear();
        this.bookHiredList.clear();
        this.cartEntityControllers.clear();

        this.username = null;
        this.token = null;
    }
}
