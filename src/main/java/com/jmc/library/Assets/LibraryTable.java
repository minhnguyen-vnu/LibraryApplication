package com.jmc.library.Assets;

import com.jmc.library.Controllers.Assets.LoadingController;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUtlis;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import  javafx.scene.image.ImageView;
import javafx.scene.control.Label;

public class LibraryTable {
    public TableColumn<BookInfo, Integer> book_id_tb_cl;
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public TableColumn<BookInfo, String> author_tb_cl;
    public TableColumn<BookInfo, Integer> quantity_tb_cl;
    public TableColumn<BookInfo, Double> least_price_tb_cl;
    public TableColumn<BookInfo, LocalDate> published_date_tb_cl;
    public TableView<BookInfo> store_tb;
    public ObservableList<BookInfo> bookList;
    public ImageView loading_img;

    protected void addLoading() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Loading.fxml"));
        try {
            loading_img = loader.load();
            store_tb.setPlaceholder(loading_img);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected void setTable() {
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);
    }

    protected void addBinding(){
        book_id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        quantity_tb_cl.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        least_price_tb_cl.setCellValueFactory(new PropertyValueFactory<>("leastPrice"));
        published_date_tb_cl.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
    }

    protected void showLibrary() {
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select * from bookStore");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    BookInfo currentBook = new BookInfo(resultSet.getInt("bookId"), resultSet.getString("bookName"),
                            resultSet.getString("authorName"), resultSet.getInt("quantityInStock"), resultSet.getDouble("leastPrice"),
                            resultSet.getDate("publishDate").toLocalDate());
                    bookList.add(currentBook);
                }
                resultSet.close();
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        });
        dbQuery.setOnFailed(event -> {
            System.out.println("Failed");
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }
}
