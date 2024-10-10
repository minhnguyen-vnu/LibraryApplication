package com.jmc.library.Assets;

import java.time.LocalDate;

public class UserBookInfo {
    private String bookName;
    private String authorName;
    private int bookId;
    private LocalDate returnDate, pickedDate;
    private double totalCost;

    public UserBookInfo(String bookName, String authorName, int bookId, LocalDate pickedDate, LocalDate returnDate, double totalCost) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.bookId = bookId;
        this.returnDate = pickedDate;
        this.pickedDate = returnDate;
        this.totalCost = totalCost;
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

    @Override
    public String toString() {
        return "UserBookInfo{" +
                "bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", bookId=" + bookId +
                ", returnDate=" + returnDate +
                ", pickedDate=" + pickedDate +
                ", totalCost=" + totalCost +
                '}';
    }
}
