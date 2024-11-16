package com.jmc.library.Assets;

import com.jmc.library.Controllers.GoogleBookAPI.GoogleBookAPIMethod;
import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;

public class GoogleBookInfo {
    private int bookId;
    private String bookName;
    private String authorName;
    private int quantityInStock;
    private double leastPrice;
    private LocalDate publishedDate;
    private String ISBN;

    private JSONObject bookInfo;
    private JSONArray items;
    private JSONObject volumeInfo;


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

    private void getInfo() {
        this.bookInfo = new GoogleBookAPIMethod().searchBook(this.ISBN);
        int indexes = new GoogleBookAPIMethod().getTotalItems(bookInfo) - 1;
        this.items = bookInfo.getJSONArray("items");
        if (items != null) {
            this.volumeInfo = items.getJSONObject(indexes).getJSONObject("volumeInfo");
            this.bookName = volumeInfo.optString("title");
            this.publishedDate = LocalDate.parse(volumeInfo.optString("publishedDate"));
            JSONArray authors = volumeInfo.optJSONArray("authors");
            if (authors != null && !authors.isEmpty()) {
                this.authorName = authors.getString(0);
            }
        }
    }
}
