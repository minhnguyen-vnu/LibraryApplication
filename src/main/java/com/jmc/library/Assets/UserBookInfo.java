package com.jmc.library.Assets;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import java.time.LocalDate;

public class UserBookInfo {
    private String bookName;
    private String authorName;
    private int bookId;
    private LocalDate returnDate, pickedDate;
    private double totalCost;
    private String requestStatus;
    private ObjectProperty<ImageView> imageView = new SimpleObjectProperty<>();

    private double singleCost;
    private String ISBN;

    public BooleanProperty rate = new SimpleBooleanProperty(false);

    public UserBookInfo() {}

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
        this.imageView.set(imageView);
        this.singleCost = totalCost;
    }

    public UserBookInfo(String bookName, String authorName,
                        int bookId, LocalDate pickedDate,
                        LocalDate returnDate, double totalCost,
                        String status, boolean isRate, ImageView imageView) {
        this(bookName, authorName, bookId, pickedDate, returnDate, totalCost, status, imageView);
        this.setRate(isRate);
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public double getSingleCost() {
        return singleCost;
    }

    public void setSingleCost(double singleCost) {
        this.singleCost = singleCost;
    }

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

    public boolean getRate() {
        return rate.get();
    }

    public void setRate(boolean value) {
        this.rate.set(value);
    }

    public BooleanProperty rateProperty() {
        return rate;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public void setImageView(ImageView imageView) {
        this.imageView.set(imageView);
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
