package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.DataBase.*;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

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
        loadCartEntityControllers();
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
                    "    r.requestStatus\n"+
                    "from userRequest r\n" +
                    "join bookStore b using(bookId)\n" +
                    "where r.username = ? order by r.requestStatus;", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
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
                    "and r.requestStatus = 'accepted';", this.username);
            while (resultSet.next()) {
                UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                        resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                        resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"),
                        resultSet.getString("status"));
                this.bookHiredList.add(userBookInfo);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadCartEntityControllers() {
        cartEntityControllers.clear();
        DBQuery dbQuery = new DBQuery("select\n" +
                "    r.username,\n" +
                "    r.bookName,\n" +
                "    b.authorName,\n" +
                "    r.bookId,\n" +
                "    r.pickedDate,\n" +
                "    r.returnDate,\n" +
                "    r.cost,\n" +
                "    r.requestStatus\n"+
                "from userRequest r\n" +
                "join bookStore b using(bookId)\n" +
                "where r.username = ? " +
                "and r.requestStatus = 'Need_to_payment';", this.username);
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                            resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                            resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"),
                            resultSet.getString("requestStatus"));
                    CartEntityController cartEntityController = new CartEntityController(userBookInfo);
                    this.cartEntityControllers.add(cartEntityController);
                }
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.start();
        System.out.println(cartEntityControllers.size());
    }

    public void removeCartEntityController(CartEntityController cartEntityController) {
        DBUpdate dbUpdate = new DBUpdate("delete from userRequest where username = ? and bookId = ?",
                this.username, cartEntityController.getUserBookInfo().getBookId());
        Thread thread = new Thread(dbUpdate);
        thread.start();
        this.cartEntityControllers.remove(cartEntityController);
    }

    public String getSubTotal() {
        double total = this.cartEntityControllers.stream().mapToDouble(cartEntityController -> cartEntityController.getUserBookInfo().getTotalCost()).sum();
        System.out.println(total);
        String result = Double.toString(total);
        System.out.println(result);
        return result;

    }

    public String getAdditional() {
        return "0.00";
    }

    public String getTotal() {
        return String.format("%.2f",Double.parseDouble(getSubTotal()) + Double.parseDouble(getAdditional()));
    }

    public void userPayment() {
        DBUpdate dbUpdate = new DBUpdate("update userRequest set requestStatus = 'pending' where username = ? and requestStatus = 'Need_to_payment';", this.username);
        Thread thread = new Thread(dbUpdate);
        thread.start();
        for(CartEntityController cartEntityController : this.cartEntityControllers) {
            cartEntityController.getUserBookInfo().setRequestStatus("pending");
            LibraryModel.getInstance().getUser().getBookPendingList()
                    .add(cartEntityController.getUserBookInfo());
        }
    }

    public void rebuiltCart() {
        for(CartEntityController cartEntityController : this.cartEntityControllers) {
            try {
                cartEntityController.updReturnDateDB(cartEntityController.getUserBookInfo().getReturnDate());
                cartEntityController.updCostDB();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
    public void clearCart() {
        DBUpdate dbUpdate = new DBUpdate("delete from userRequest where username = ? and requestStatus = 'Need_to_payment';", this.username);
        Thread thread = new Thread(dbUpdate);
        thread.start();
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
