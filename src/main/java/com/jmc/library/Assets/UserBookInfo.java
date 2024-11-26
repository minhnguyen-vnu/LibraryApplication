package com.jmc.library.Assets;

import com.jmc.library.Database.*;

import javafx.scene.image.ImageView;
import java.awt.*;
import java.sql.ResultSet;
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
    private boolean isRated;

    public UserBookInfo() {

    }

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
        this.imageView = imageView;
        this.singleCost = totalCost;
    }

    public UserBookInfo(String bookName, String authorName,
                        int bookId, LocalDate pickedDate,
                        LocalDate returnDate, double totalCost,
                        String status, boolean isRated, ImageView imageView) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookId = bookId;
        this.pickedDate = pickedDate;
        this.returnDate = returnDate;
        this.requestStatus = status;
        this.totalCost = totalCost;
        this.isRated = isRated;
        this.imageView = imageView;
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

    public boolean getRated() {
        return isRated;
    }

    public void setRated(boolean isRated) {
        this.isRated = isRated;
    }

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
