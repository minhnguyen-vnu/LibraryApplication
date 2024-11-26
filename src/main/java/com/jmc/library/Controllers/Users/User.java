package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.*;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class User {
//    public TableView<UserBookInfo> store_tb;
    private String username;
    private String password;
    private String name;
    private LocalDate birthDate;
    private int ID;
    private Image avatar;

    private ObservableList<CartEntityController> cartEntityControllers;

    public User() {
        this.cartEntityControllers = FXCollections.observableArrayList();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        if(name == null) {
            return username;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        if(birthDate == null) {
            return LocalDate.now().plusDays(1);
        }
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Image getAvatar() {
        if(avatar == null) {
            return new Image(Objects.requireNonNull(getClass().getResource("/IMAGES/avatar.png")).toExternalForm());
        }
        return avatar;
    }

    public void setAvatar(Image avatar) {
        this.avatar = avatar;
    }

    public ObservableList<CartEntityController> getCartEntityControllers() {

        return cartEntityControllers;
    }

    public void setCartEntityControllers(ObservableList<CartEntityController> cartEntityControllers) {
        this.cartEntityControllers = cartEntityControllers;
    }

    public void loadUserInfo() {
        DBQuery dbQuery = new DBQuery("select * from users where username = ?", this.username);
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                if (resultSet.next()) {
                    this.username = resultSet.getString("username");
                    this.password = resultSet.getString("password");
                    System.out.println("User.loadUserInfo " + this.password);
                    this.name = (resultSet.getString("name") == null) ? "" : resultSet.getString("name");
                    this.birthDate = (resultSet.getDate("birthDate") == null) ? LocalDate.now().plusDays(1) : resultSet.getDate("birthDate").toLocalDate();
                    this.ID = resultSet.getInt("ID");
                    Blob blob = resultSet.getBlob("imageView");

                    if (blob != null && blob.length() > 0) {
                        byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                        this.avatar = ImageUtils.byteArrayToImage(imageBytes);
                    } else {
                        this.avatar = new Image(Objects.requireNonNull(getClass().getResource("/IMAGES/avatar.png")).toExternalForm());
                    }
                }
                resultSet.close();
                System.out.println("User.loadUserInfo" + this.password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
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
            Model.getInstance().getViewFactory().getUserPendingController().getBookList()
                    .add(cartEntityController.getUserBookInfo());

            DBUpdate dbUpdate = new DBUpdate("INSERT INTO PendingRequest(bookId, bookName, username, requestDate, returnDate, cost, requestStatus) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)",
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
        }
    }

    public void clearCart() {
        this.cartEntityControllers.clear();
    }

    public void resetAll() {
        System.out.println("User.resetAll");

        this.cartEntityControllers.clear();

        this.username = null;
    }
}
