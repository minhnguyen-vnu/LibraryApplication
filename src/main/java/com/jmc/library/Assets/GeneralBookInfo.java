package com.jmc.library.Assets;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;

import java.time.LocalDate;

public class GeneralBookInfo {
    protected int bookId;
    protected String bookName;
    protected String authorName;
    protected IntegerProperty quantityInStock = new SimpleIntegerProperty();
    protected double leastPrice;
    protected LocalDate publishedDate;
    protected String ISBN;
    protected String publisher;
    protected boolean exist;
    protected String genre;
    protected String originalLanguage;
    protected String description;
    protected String thumbnail;

    public GeneralBookInfo() {

    }

    public GeneralBookInfo(int bookId, int quantityInStock, double leastPrice, String ISBN) {
        this.bookId = bookId;
        this.quantityInStock.set(quantityInStock);
        this.leastPrice = leastPrice;
        this.ISBN = ISBN;
    }

    public GeneralBookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock.set(quantityInStock);
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
        this.exist = true;
    }

    public GeneralBookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN, String publisher, String genre, String originalLanguage, String description, String thumbnail) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock.set(quantityInStock);
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.genre = genre;
        this.originalLanguage = originalLanguage;
        this.description = description;
        this.thumbnail = thumbnail;
        this.exist = true;
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
        return quantityInStock.get();
    }

    public void setQuantityInStock(int quantityInStock) {
        this.quantityInStock.set(quantityInStock);
    }

    public IntegerProperty quantityInStockProperty() {
        return quantityInStock;
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

    public boolean isExist() { return exist; }

    public String getISBN() { return ISBN; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getPublisher() { return publisher; }

    public String getGenre() { return genre; }

    public String getOriginalLanguage() { return originalLanguage; }

    public String getDescription() { return description; }

    public String getThumbnail() { return thumbnail; }
}
