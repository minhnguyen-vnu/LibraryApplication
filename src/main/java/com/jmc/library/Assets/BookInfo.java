package com.jmc.library.Assets;

import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class BookInfo {
    private int bookId;
    private String bookName;
    private String authorName;
    private String ISBN;
    private String publisher;
    private String genre;
    private LocalDate publishedDate;
    private String originalLanguage;
    private String description;
    private String thumbnail;

    private JSONObject bookInfo;
    private JSONArray items;
    private JSONObject volumeInfo;

    private int quantityInStock;
    private double leastPrice;

    public BookInfo() {
        bookId = 0;
        bookName = "";
        authorName = "";
        ISBN = "";
        publisher = "";
        genre = "";
        publishedDate = LocalDate.now();
        originalLanguage = "";
        description = "";
        thumbnail = "";
        quantityInStock = 0;
        leastPrice = 0.0;
    }

    public BookInfo(int bookId, String bookName, LocalDate publishedDate, String authorName, int quantityInStock, double leastPrice, String ISBN) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.quantityInStock = quantityInStock;
        this.leastPrice = leastPrice;
        this.publishedDate = publishedDate;
        this.ISBN = ISBN;
        this.genre = "";
        this.publisher = "";
        this.originalLanguage = "";
        this.description = "";
        this.thumbnail = "";
        setBookInfo();
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

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

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

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage() {
        originalLanguage = volumeInfo.has("language") ? volumeInfo.getString("language") : "N/A";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription() {
        description = volumeInfo.has("description") ? volumeInfo.getString("description") : "No description available";
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail() {
        thumbnail = volumeInfo.has("imageLinks") ? volumeInfo.getJSONObject("imageLinks").getString("thumbnail") : null;
    }

    public JSONObject getBookInfo() {
        return bookInfo;
    }

    public JSONArray getItems() {
        return items;
    }

    public void setBookInfo() {
        this.bookInfo = new GoogleBookAPIMethod().searchBook(this.ISBN);
        int totalItems = new GoogleBookAPIMethod().getTotalItems(bookInfo) - 1;
        this.items = bookInfo.getJSONArray("items");
        if (items != null) {
            this.volumeInfo = items.getJSONObject(totalItems).getJSONObject("volumeInfo");
            setPublisher();
            setGenre();
            setOriginalLanguage();
            setDescription();
            setThumbnail();
        }
    }
}
