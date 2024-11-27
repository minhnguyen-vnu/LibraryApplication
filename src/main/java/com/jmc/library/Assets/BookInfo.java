package com.jmc.library.Assets;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Class for managing the book information.
 */
public class BookInfo {
    private int bookId;
    private String bookName;
    private String authorName;
    private int quantityInStock;
    private double leastPrice;
    private LocalDate publishedDate;
    private String ISBN;
    private String publisher;
    private String genre;
    private String originalLanguage;
    private String description;
    private String thumbnail;
    private boolean exist;
    private ObjectProperty<ImageView> imageView;
    private SimpleDoubleProperty rating = new SimpleDoubleProperty();
    private int rateQuantities;

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
    }

    public BookInfo() {
        this.exist = false;
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
        this.exist = true;
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN, ImageView imageView) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
        this.imageView =  new SimpleObjectProperty<>(imageView);
        this.exist = true;
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN, String publisher, String genre, String originalLanguage, String description, String thumbnail, ImageView imageView) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.genre = genre;
        this.originalLanguage = originalLanguage;
        this.description = description;
        this.thumbnail = thumbnail;
        this.imageView =  new SimpleObjectProperty<>(imageView);
        this.exist = true;
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN, String publisher, String genre, String originalLanguage, String description, String thumbnail, ImageView imageView, double rating, int rateQuantities) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
        this.publisher = publisher;
        this.genre = genre;
        this.originalLanguage = originalLanguage;
        this.description = description;
        this.thumbnail = thumbnail;
        this.imageView =  new SimpleObjectProperty<>(imageView);
        this.exist = true;
        this.rating.set(rating);
        this.rateQuantities = rateQuantities;
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

    public double getRating() { return rating.get(); }

    public void setRating(double rating) { this.rating.set(rating); }

    public SimpleDoubleProperty doubleProperty() { return rating; }

    public int getRateQuantities() { return rateQuantities; }

    public void setRateQuantities(int rateQuantities) { this.rateQuantities = rateQuantities; }

    public boolean isExist() { return exist; }

    public String getISBN() { return ISBN; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public String getPublisher() { return publisher; }

    public void setPublisher(String publisher) { this.publisher = publisher; }

    public String getGenre() { return genre; }

    public void setGenre(String genre) { this.genre = genre; }

    public String getOriginalLanguage() { return originalLanguage; }

    public void setOriginalLanguage(String originalLanguage) { this.originalLanguage = originalLanguage; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public ImageView getImageView() { return imageView.get(); }

    public void setImageView(ImageView imageView) { this.imageView.set(imageView); }

    public ObjectProperty<ImageView> imageViewProperty() { return imageView; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInfo bookInfo = (BookInfo) o;
        return bookId == bookInfo.bookId && quantityInStock == bookInfo.quantityInStock && Double.compare(leastPrice, bookInfo.leastPrice) == 0 && Objects.equals(bookName, bookInfo.bookName) && Objects.equals(authorName, bookInfo.authorName) && Objects.equals(publishedDate, bookInfo.publishedDate) && Objects.equals(ISBN, bookInfo.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookId, bookName, authorName, quantityInStock, leastPrice, publishedDate, ISBN);
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
                ", ISBN='" + ISBN + '\'' +
                '}';
    }
}