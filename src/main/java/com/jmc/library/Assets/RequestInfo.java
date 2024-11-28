package com.jmc.library.Assets;

import java.time.LocalDate;

public class RequestInfo {
    private int issueId;
    private int bookId;
    private String bookName;
    private String username;
    private LocalDate returnDate, pickedDate;
    private double totalCost;
    private String requestStatus;

    public RequestInfo(int issueID, int bookId, String bookName, String username, LocalDate pickedDate, LocalDate returnDate, double totalCost, String requestStatus) {
        this.issueId = issueID;
        this.bookId = bookId;
        this.bookName = bookName;
        this.username = username;
        this.returnDate = returnDate;
        this.pickedDate = pickedDate;
        this.totalCost = Math.round(totalCost * 100.0) / 100.0;
        this.requestStatus = requestStatus;
    }

    public int getIssueId() { return issueId; }

    public void setIssueId(int issueId) { this.issueId = issueId; }

    public String getBookName() { return bookName; }

    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getRequestStatus() { return requestStatus; }

    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }

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
        return "RequestInfo{" +
                "issueId=" + issueId +
                ", bookId=" + bookId +
                ", username='" + username + '\'' +
                ", returnDate=" + returnDate +
                ", pickedDate=" + pickedDate +
                ", totalCost=" + totalCost +
                ", requestStatus='" + requestStatus + '\'' +
                '}';
    }
}
