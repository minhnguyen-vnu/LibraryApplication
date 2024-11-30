package com.jmc.library.Assets;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Class for managing the book information.
 */
public class BookInfo extends GeneralBookInfo {
    private ObjectProperty<ImageView> imageView = new SimpleObjectProperty<>();
    private SimpleDoubleProperty rating = new SimpleDoubleProperty();
    private int rateQuantities;

    public BookInfo() {
        super();
        this.exist = false;
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN) {
        super(bookId, bookName, authorName, quantityInStock, leastPrice, publishedDate, ISBN);
        this.exist = true;
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN, String publisher, String genre, String originalLanguage, String description, String thumbnail, ImageView imageView) {
        super(bookId, bookName, authorName, quantityInStock, leastPrice, publishedDate, ISBN, publisher, genre, originalLanguage, description, thumbnail);
        this.imageView =  new SimpleObjectProperty<>(imageView);
        this.exist = true;
    }

    public BookInfo(int bookId, String bookName, String authorName, int quantityInStock, double leastPrice, LocalDate publishedDate, String ISBN, String publisher, String genre, String originalLanguage, String description, String thumbnail, ImageView imageView, double rating, int rateQuantities) {
        super(bookId, bookName, authorName, quantityInStock, leastPrice, publishedDate, ISBN, publisher, genre, originalLanguage, description, thumbnail);
        this.imageView =  new SimpleObjectProperty<>(imageView);
        this.exist = true;
        this.rating.set(rating);
        this.rateQuantities = rateQuantities;
    }

    public double getRating() { return rating.get(); }

    public void setRating(double rating) { this.rating.set(rating); }

    public SimpleDoubleProperty doubleProperty() { return rating; }

    public int getRateQuantities() { return rateQuantities; }

    public void setRateQuantities(int rateQuantities) { this.rateQuantities = rateQuantities; }

    public ImageView getImageView() { return imageView.get(); }

    public void setImageView(ImageView imageView) { this.imageView.set(imageView); }

    public ObjectProperty<ImageView> imageViewProperty() { return imageView; }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookInfo bookInfo = (BookInfo) o;
        return bookId == bookInfo.bookId && quantityInStock.get() == bookInfo.quantityInStock.get() && Double.compare(leastPrice, bookInfo.leastPrice) == 0 && Objects.equals(bookName, bookInfo.bookName) && Objects.equals(authorName, bookInfo.authorName) && Objects.equals(publishedDate, bookInfo.publishedDate) && Objects.equals(ISBN, bookInfo.ISBN);
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