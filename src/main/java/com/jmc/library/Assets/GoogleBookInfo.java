package com.jmc.library.Assets;

import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import org.json.JSONArray;
import org.json.JSONObject;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.time.LocalDate;

public class GoogleBookInfo {
    private int bookId;
    private String bookName;
    private String authorName;
    private int quantityInStock;
    private double leastPrice;
    private LocalDate publishedDate;
    private String ISBN;
    private boolean exist;
    private boolean loaded;

    private JSONObject bookInfo;
    private JSONArray items;
    private JSONObject volumeInfo;

    private String publisher;
    private String genre;
    private String originalLanguage;
    private String description;
    private String thumbnail;
    private ImageView imageView;


    public GoogleBookInfo() {};

    public GoogleBookInfo(int bookId, int quantityInStock, double leastPrice, String ISBN) {
        this.bookId = bookId;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.ISBN = ISBN;
        getInfo();
    }

    public int getBookId() { return bookId; }

    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getBookName() { return bookName; }

    public String getAuthorName() { return authorName; }

    public int getQuantityInStock() { return quantityInStock; }

    public void setQuantityInStock(int quantityInStock) { this.quantityInStock = quantityInStock; }

    public double getLeastPrice() { return leastPrice; }

    public void setLeastPrice(double leastPrice) { this.leastPrice = leastPrice; }

    public LocalDate getPublishedDate() { return publishedDate; }

    public String getISBN() { return ISBN; }

    public void setISBN(String ISBN) { this.ISBN = ISBN; }

    public boolean isExist() { return exist; }

    public void setExist(boolean exist) { this.exist = exist; }

    public boolean isLoaded() { return loaded; }

    public void setLoaded(boolean loaded) { this.loaded = loaded; }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher() {
        this.publisher = volumeInfo.has("publisher") ? volumeInfo.getString("publisher") : "N/A";
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre() {
        if (volumeInfo.has("categories")) {
            JSONArray categoriesArray = volumeInfo.getJSONArray("categories");
            this.genre = categoriesArray.join(", ").replace("\"", "");
        } else {
            this.genre = "N/A";
        }
    }

    public String getOriginalLanguage() { return originalLanguage; }

    public void setOriginalLanguage() {
        originalLanguage = volumeInfo.has("language") ? volumeInfo.getString("language") : "N/A";
    }

    public String getDescription() { return description; }

    public void setDescription() {
        description = volumeInfo.has("description") ? volumeInfo.getString("description") : "No description available";
    }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail() {
        thumbnail = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : null;
    }

    public ImageView getImageView() { return this.imageView; }

    public void setImageView() {
        if (this.thumbnail != null && !this.thumbnail.isEmpty()) {
            this.imageView = new ImageView(new Image(this.thumbnail, 50, 75, true, true));
        }
        else {
            this.imageView = new ImageView(getClass().getResource("/IMAGES/UnknownBookCover.png").toExternalForm());
        }
    }

    private void getInfo() {
        this.bookInfo = new GoogleBookAPIMethod().searchBook(this.ISBN);
        int indexes = new GoogleBookAPIMethod().getTotalItems(bookInfo) - 1;
        this.items = bookInfo.getJSONArray("items");
        if (items != null) {
            this.exist = true;
            this.volumeInfo = items.getJSONObject(indexes).getJSONObject("volumeInfo");
            this.bookName = volumeInfo.optString("title");
            this.publishedDate = LocalDate.parse(volumeInfo.optString("publishedDate"));
            JSONArray authors = volumeInfo.optJSONArray("authors");
            if (authors != null && !authors.isEmpty()) {
                this.authorName = authors.getString(0);
            }
            setPublisher();
            setGenre();
            setOriginalLanguage();
            setDescription();
            setThumbnail();
            setImageView();
        }
        else {
            this.exist = false;
        }
        this.loaded = true;
    }
}