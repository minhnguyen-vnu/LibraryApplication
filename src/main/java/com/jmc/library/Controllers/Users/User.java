package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.DBUtlis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String username;
    private String token;
    private ObservableList<UserBookInfo> bookPendingList;
    private ObservableList<UserBookInfo> bookHiredList;
    private ObservableList<UserBookInfo> bookWaitingPaymentList;
    private ObservableList<CartEntityController> cartEntityControllers;

    public User() {
        this.bookPendingList = FXCollections.observableArrayList();
        this.bookHiredList = FXCollections.observableArrayList();
        this.bookWaitingPaymentList = FXCollections.observableArrayList();
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

    public ObservableList<UserBookInfo> getBookWaitingPaymentList() {
        return bookWaitingPaymentList;
    }

    public void setBookWaitingPaymentList(ObservableList<UserBookInfo> bookWaitingPaymentList) {
        this.bookWaitingPaymentList = bookWaitingPaymentList;
    }

    public ObservableList<CartEntityController> getCartEntityControllers() {
        return cartEntityControllers;
    }

    public void setCartEntityControllers(ObservableList<CartEntityController> cartEntityControllers) {
        this.cartEntityControllers = cartEntityControllers;
    }

    public void loadBookPendingList() {
        bookPendingList.clear();
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                    "    r.username,\n" +
                    "    r.bookName,\n" +
                    "    b.authorName,\n" +
                    "    r.bookId,\n" +
                    "    r.pickedDate,\n" +
                    "    r.returnDate,\n" +
                    "    r.cost,\n" +
                    "    r.status\n"+
                    "from userRequest r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ? order by r.status;", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"), resultSet.getString("status"));
                this.bookPendingList.add(userBookInfo);
            }
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
                    "    r.status\n"+
                    "from userHired r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ? " +
                    "and r.status = 'accepted';", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"),
                        resultSet.getString("status"));
                this.bookHiredList.add(userBookInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadBookWaitingPaymentList() {
        bookWaitingPaymentList.clear();
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                    "    r.username,\n" +
                    "    r.bookName,\n" +
                    "    b.authorName,\n" +
                    "    r.bookId,\n" +
                    "    r.pickedDate,\n" +
                    "    r.returnDate,\n" +
                    "    r.cost,\n" +
                    "from userHired r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ? " +
                    "and r.status = 'waiting_payment';", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"),
                        resultSet.getString("status"));
                this.bookWaitingPaymentList.add(userBookInfo);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCartEntityControllers() {
        cartEntityControllers.clear();
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select\n" +
                    "    r.username,\n" +
                    "    r.bookName,\n" +
                    "    b.authorName,\n" +
                    "    r.bookId,\n" +
                    "    r.pickedDate,\n" +
                    "    r.returnDate,\n" +
                    "    r.cost,\n" +
                    "    r.status\n"+
                    "from userRequest r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ? " +
                    "and r.status = 'Need_to_payment';", this.username);
            while (resultSet.next()) {
                System.out.println(1);
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"),
                        resultSet.getString("status"));
                CartEntityController cartEntityController = new CartEntityController(userBookInfo);
                this.cartEntityControllers.add(cartEntityController);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println(cartEntityControllers.size());
    }

    public void removeCartEntityController(CartEntityController cartEntityController) {
        DBUtlis.executeUpdate("delete from userRequest where username = ? and bookId = ?",
                this.username, cartEntityController.getUserBookInfo().getBookId());
        this.cartEntityControllers.remove(cartEntityController);
    }

    public String getSubTotal() {
        return String.format("%.2f",this.cartEntityControllers.stream().mapToDouble(cartEntityController -> cartEntityController.getUserBookInfo().getTotalCost()).sum());
    }

    public String getAdditional() {
        return "0.00";
    }

    public String getTotal() {
        return String.format("%.2f",Double.parseDouble(getSubTotal()) + Double.parseDouble(getAdditional()));
    }
}
