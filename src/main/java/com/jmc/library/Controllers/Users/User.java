package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.*;
import com.jmc.library.Models.CartModel;
import com.jmc.library.Models.DashboardModel;
import com.jmc.library.Models.LibraryModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a user in the library system, including their personal information, avatar, and book lists.
 */
public class User {
    private String username;
    private String password;
    private final SimpleStringProperty name;
    private LocalDate birthDate;
    private int ID;
    private final SimpleObjectProperty<Image> avatar;
    private double totalPaid;
    private int totalBorrowed;
    private String status;

    private ObservableList<UserBookInfo> PendingBookList;
    private ObservableList<UserBookInfo> BorrowedBookList;

    public User() {
        this.PendingBookList = FXCollections.observableArrayList();
        this.BorrowedBookList = FXCollections.observableArrayList();
        avatar = new SimpleObjectProperty<>();
        name = new SimpleStringProperty();
    }

    public User(String username, String password, String name, LocalDate birthDate, int ID, Image avatar, double totalPaid, int totalBorrowed, String status) {
        this.username = username;
        this.password = password;
        this.name = new SimpleStringProperty(name);
        this.birthDate = birthDate;
        this.ID = ID;
        this.avatar = new SimpleObjectProperty<>(avatar);
        this.totalPaid = totalPaid;
        this.totalBorrowed = totalBorrowed;
        this.status = status;
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
        if (name.get().isEmpty()) {
            return username;
        }
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public LocalDate getBirthDate() {
        if (birthDate == null) {
            return LocalDate.now().plusDays(1);
        }
        return birthDate;
    }

    public SimpleStringProperty nameProperty() {
        return name;
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
        return avatar.get();
    }

    public void setAvatar(Image avatar) {
        this.avatar.set(avatar);
    }

    public SimpleObjectProperty<Image> avatarProperty() {
        return avatar;
    }

    public ObservableList<UserBookInfo> getPendingBookList() {
        return PendingBookList;
    }

    public void setPendingBookList(ObservableList<UserBookInfo> bookList) {
        this.PendingBookList = bookList;
    }

    public ObservableList<UserBookInfo> getBorrowedBookList() {
        return BorrowedBookList;
    }

    public void setBorrowedBookList(ObservableList<UserBookInfo> borrowedBookList) {
        this.BorrowedBookList = borrowedBookList;
    }

    /**
     * Loads user information from the given ResultSet.
     *
     * @param resultSet The ResultSet containing user information.
     * @throws SQLException If an SQL error occurs.
     */
    public void loadUserInfo(ResultSet resultSet) throws SQLException {
        this.username = resultSet.getString("username");
        this.password = resultSet.getString("password");
        String fetchedName = (resultSet.getString("name") == null) ? "" : resultSet.getString("name");
        this.name.set(fetchedName);
        this.birthDate = (resultSet.getDate("birthDate") == null) ? LocalDate.now().plusDays(1) : resultSet.getDate("birthDate").toLocalDate();
        this.ID = resultSet.getInt("ID");
        Blob blob = resultSet.getBlob("imageView");

        if (blob != null && blob.length() > 0) {
            byte[] imageBytes = blob.getBytes(1, (int) blob.length());
            this.avatar.set(ImageUtils.byteArrayToImage(imageBytes));
            if (this.getAvatar() == null) {
                System.out.println("Avatar image is null");
            } else {
                // Convert the avatar image to a byte array
                byte[] imageBytes1 = ImageUtils.imageToByteArray(this.getAvatar());
                // Check if the byte array is empty
                if (imageBytes1.length == 0) {
                    System.out.println("Converted byte array is empty");
                } else {
                    System.out.println(Arrays.toString(imageBytes1).charAt(30));
                }
            }
        } else {
            this.avatar.set(new Image(Objects.requireNonNull(getClass().getResource("/IMAGES/avatar.png")).toExternalForm()));
        }
        resultSet.close();
    }

    /**
     * Loads user information from the database.
     */
    public void loadUserInfo() {
        DBQuery dbQuery = new DBQuery("select * " +
                "from users where username = ?", this.username);
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                if (resultSet.next()) {
                    loadUserInfo(resultSet);
                }
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();

    }

    /**
     * Loads the list of pending books for the user from the database.
     */
    public void loadPendingBooks() {
        DBQuery dbQuery = new DBQuery("select\n" +
                "    r.username,\n" +
                "    r.bookName,\n" +
                "    b.authorName,\n" +
                "    r.bookId,\n" +
                "    r.requestDate,\n" +
                "    r.returnDate,\n" +
                "    r.cost,\n" +
                "    r.requestStatus, \n" +
                "    b.imageView\n" +
                "from PendingRequest r\n" +
                "join bookStore b using(bookId)\n" +
                "where r.username = ? order by r.requestStatus;", LibraryModel.getInstance().getUser().getUsername());
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    Blob blob = resultSet.getBlob("imageView");
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    Image image = ImageUtils.byteArrayToImage(imageBytes);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(75);
                    imageView.setFitWidth(50);
                    UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                            resultSet.getInt("bookId"), resultSet.getDate("requestDate").toLocalDate(),
                            resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"), resultSet.getString("requestStatus"), imageView);
                    this.PendingBookList.add(userBookInfo);
                }
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Loads the list of hired books for the user from the database.
     */
    public void loadBorrowedBook() {
        DBQuery dbQuery = new DBQuery("select\n" +
                "    r.username,\n" +
                "    r.bookName,\n" +
                "    b.authorName,\n" +
                "    r.bookId,\n" +
                "    r.pickedDate,\n" +
                "    r.returnDate,\n" +
                "    r.cost,\n" +
                "    r.requestStatus,\n" +
                "    r.isRated, \n" +
                "    b.imageView\n" +
                "from  userRequest r\n" +
                "join bookStore b using(bookId)\n" +
                "where r.username = ? " +
                "order by r.requestStatus;", LibraryModel.getInstance().getUser().getUsername());
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    Blob blob = resultSet.getBlob("imageView");
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    Image image = ImageUtils.byteArrayToImage(imageBytes);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(75);
                    imageView.setFitWidth(50);
                    UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                            resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                            resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"), resultSet.getString("requestStatus"), resultSet.getBoolean("isRated"), imageView);
                    this.BorrowedBookList.add(userBookInfo);
                }
                DashboardModel.getInstance().getUserDashboardInfo().onDashBoardSetUp();
                resultSet.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Processes user payment and updates the database with the new pending requests.
     */
    public void userPayment() {
        for (CartEntityController cartEntityController : CartModel.getInstance().getUserCartInfo().getCartList()) {
            LibraryModel.getInstance().getUser().getPendingBookList()
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
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * Resets all user information and clears the book lists.
     */
    public void resetAll() {
        this.PendingBookList.clear();
        this.BorrowedBookList.clear();
        CartModel.getInstance().getUserCartInfo().getCartList().clear();

        this.username = null;

        DashboardModel.getInstance().getUserDashboardInfo().clear();
        CartModel.getInstance().getUserCartInfo().clear();
    }

    public double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(int totalPaid) {
        this.totalPaid = totalPaid;
    }

    public int getTotalBorrowed() {
        return totalBorrowed;
    }

    public void setTotalBorrowed(int totalBorrowed) {
        this.totalBorrowed = totalBorrowed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
