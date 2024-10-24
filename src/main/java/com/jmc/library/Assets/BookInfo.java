package com.jmc.library.Assets;

import java.time.LocalDate;

public class BookInfo {
    private int bookId;
    private String bookName;
    private String authorName;
    private int quantityInStock;
    private double leastPrice;
    private LocalDate publishedDate;
    private boolean inCart;
    private String ISBN;

    public BookInfo() {
        this.bookId = 0;
        this.bookName = "";
        this.authorName = "";
        this.quantityInStock = 0;
        this.leastPrice = 0.0;
        this.publishedDate = LocalDate.now();
        this.ISBN = "";
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock = quantityInStock;
    }

    public double getLeastPrice() {
        return leastPrice;
    }

    public void setLeastPrice(double leastPrice) {
        this.leastPrice = leastPrice;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public boolean isInCart() { return inCart; }

    public void setInCart(boolean inCart) { this.inCart = inCart; }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }
}
