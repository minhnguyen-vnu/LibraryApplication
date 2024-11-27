package com.jmc.library.Assets;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.ImageView;
import java.time.LocalDate;

public class UserBookInfo {
    private String bookName;
    private String authorName;
    private int bookId;
    private LocalDate returnDate, pickedDate;
    private double totalCost;
    private String requestStatus;
    private ImageView imageView;

    private double singleCost;
    private String ISBN;

    // Chuyển `isRated` thành thuộc tính observable
    private final BooleanProperty rated = new SimpleBooleanProperty(false);

    // Constructor mặc định
    public UserBookInfo() {}

    // Constructor đầy đủ
    public UserBookInfo(String bookName, String authorName,
                        int bookId, LocalDate pickedDate,
                        LocalDate returnDate, double totalCost,
                        String status, ImageView imageView) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookId = bookId;
        this.pickedDate = pickedDate;
        this.returnDate = returnDate;
        this.totalCost = totalCost;
        this.requestStatus = status;
        imageView.setFitHeight(75);
        imageView.setFitWidth(50);
        this.imageView = imageView;
        this.singleCost = totalCost;
    }

    // Constructor có thêm `rated`
    public UserBookInfo(String bookName, String authorName,
                        int bookId, LocalDate pickedDate,
                        LocalDate returnDate, double totalCost,
                        String status, boolean isRated, ImageView imageView) {
        this(bookName, authorName, bookId, pickedDate, returnDate, totalCost, status, imageView);
        this.setRated(isRated); // Đồng bộ giá trị `isRated`
    }

    // Getter và Setter cho ISBN
    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    // Getter và Setter cho singleCost
    public double getSingleCost() {
        return singleCost;
    }

    public void setSingleCost(double singleCost) {
        this.singleCost = singleCost;
    }

    // Getter và Setter cho các thuộc tính cơ bản
    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public LocalDate getPickedDate() {
        return pickedDate;
    }

    public void setPickedDate(LocalDate pickedDate) {
        this.pickedDate = pickedDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String status) {
        this.requestStatus = status;
    }

    // Getter và Setter cho thuộc tính `rated` sử dụng BooleanProperty
    public boolean getRated() {
        return rated.get();
    }

    public void setRated(boolean value) {
        this.rated.set(value);
    }

    public BooleanProperty ratedProperty() {
        return rated;
    }

    // Getter và Setter cho imageView
    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    public String toString() {
        return "UserBookInfo{" +
                "bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", bookId=" + bookId +
                ", returnDate=" + returnDate +
                ", pickedDate=" + pickedDate +
                ", totalCost=" + totalCost +
                ", requestStatus='" + requestStatus + '\'' +
                '}';
    }
}
