package com.jmc.library.Assets;

import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

/**
 * Class for managing the Google book information.
 */
public class GoogleBookInfo extends GeneralBookInfo {
    private boolean loaded;

    private JSONObject bookInfo;
    private JSONArray items;
    private JSONObject volumeInfo;

    private ImageView imageView;

    private GoogleBookAPIMethod googleBookAPIMethod;

    // Constructor Injection
    public GoogleBookInfo(GoogleBookAPIMethod googleBookAPIMethod) {
        super();
        this.googleBookAPIMethod = googleBookAPIMethod;
    }

    public GoogleBookInfo() {
        super();
        this.googleBookAPIMethod = new GoogleBookAPIMethod(); // default
    }

    public GoogleBookInfo(int bookId, int quantityInStock, double leastPrice, String ISBN) {
        super(bookId, quantityInStock, leastPrice, ISBN);
        getInfo();
    }

    public boolean isLoaded() { return loaded; }

    public void setPublisher() {
        this.publisher = volumeInfo.has("publisher") ? volumeInfo.getString("publisher") : "N/A";
    }

    public void setGenre() {
        if (volumeInfo.has("categories")) {
            JSONArray categoriesArray = volumeInfo.getJSONArray("categories");
            this.genre = categoriesArray.join(", ").replace("\"", "");
        } else {
            this.genre = "N/A";
        }
    }

    public void setOriginalLanguage() {
        originalLanguage = volumeInfo.has("language") ? volumeInfo.getString("language") : "N/A";
    }

    public void setDescription() {
        description = volumeInfo.has("description") ? volumeInfo.getString("description") : "No description available";
    }

    public void setThumbnail() {
        thumbnail = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : null;
    }

    public ImageView getImageView() { return this.imageView; }

    public void setImageView() {
        if (this.thumbnail != null && !this.thumbnail.isEmpty()) {
            this.imageView = new ImageView(new Image(this.thumbnail, 128, 175, true, true));
        }
        else {
            this.imageView = new ImageView(getClass().getResource("/IMAGES/UnknownBookCover.png").toExternalForm());
            this.imageView.fitHeightProperty().setValue(75);
            this.imageView.fitWidthProperty().setValue(50);
        }
    }

    public void getInfo() {
        this.bookInfo = new GoogleBookAPIMethod().searchBook(this.ISBN);
        int indexes = new GoogleBookAPIMethod().getTotalItems(bookInfo) - 1;
        if (indexes < 0) {
            return;
        }
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