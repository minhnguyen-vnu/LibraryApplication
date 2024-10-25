package com.jmc.library.Assets;

import java.time.LocalDate;
import java.util.Objects;

public class BookInfo {
    private int bookId;
    private String bookName;
    private String authorName;
    private int quantityInStock;
    private double leastPrice;
    private LocalDate publishedDate;
    private boolean inCart;

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInfo bookInfo = (BookInfo) o;
        return bookId == bookInfo.bookId && quantityInStock == bookInfo.quantityInStock && Double.compare(leastPrice, bookInfo.leastPrice) == 0 && Objects.equals(bookName, bookInfo.bookName) && Objects.equals(authorName, bookInfo.authorName) && Objects.equals(publishedDate, bookInfo.publishedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookName, authorName, quantityInStock, leastPrice, publishedDate);
    }

    @Override
    public String toString() {
        return "BookInfo{" +
                "bookId=" + bookId +
                ", bookName='" + bookName + '\'' +
                ", authorName='" + authorName + '\'' +
                ", quantityInStock=" + quantityInStock +
                ", leastPrice=" + leastPrice +
                ", publishedDate=" + publishedDate +
                '}';
    }
}
