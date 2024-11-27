package com.jmc.library.Assets;

import com.jmc.library.Controllers.Assets.LoadingController;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Database.DBUtlis;
import javafx.animation.RotateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;

import java.awt.*;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import  javafx.scene.image.ImageView;
import javafx.scene.control.Label;

/**
 * Class for managing the library table.
 */
public class LibraryTable {
    public TableColumn<BookInfo, Integer> book_id_tb_cl;
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public TableColumn<BookInfo, String> author_tb_cl;
    public TableColumn<BookInfo, Integer> quantity_tb_cl;
    public TableColumn<BookInfo, Double> least_price_tb_cl;
    public TableColumn<BookInfo, LocalDate> published_date_tb_cl;
    public TableView<BookInfo> store_tb;
    public static ObservableList<BookInfo> bookList = FXCollections.observableArrayList();

    /**
     * Constructor for the LibraryTable class.
     */
    private void addLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Loading.fxml"));
        try {
            ImageView loading_img = loader.load();
            store_tb.setPlaceholder(loading_img);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor for the LibraryTable class.
     */
    private void returnLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/NoDataPlaceHolder.fxml"));
        try {
            Label label = loader.load();
            store_tb.setPlaceholder(label);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Constructor for the LibraryTable class.
     */
    protected void setTable() {
        store_tb.setItems(bookList);


        bookList.addListener(new ListChangeListener<BookInfo>() {
            @Override
            public void onChanged(Change<? extends BookInfo> change) {
                System.out.println("-23");
            }
        });
    }

    /**
     * Constructor for the LibraryTable class.
     */
    protected void addBinding() {
        book_id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        quantity_tb_cl.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        least_price_tb_cl.setCellValueFactory(new PropertyValueFactory<>("leastPrice"));
        published_date_tb_cl.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
    }

    /**
     * Constructor for the LibraryTable class.
     */
    protected void showLibrary() {
        addLoading();
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select * from bookStore");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    Blob blob = resultSet.getBlob("imageView");
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    Image image = ImageUtils.byteArrayToImage(imageBytes);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(75);
                    imageView.setFitWidth(50);
                    BookInfo currentBook = new BookInfo(resultSet.getInt(
                            "bookId"),
                            resultSet.getString("bookName"),
                            resultSet.getString("authorName"),
                            resultSet.getInt("quantityInStock"),
                            resultSet.getDouble("leastPrice"),
                            resultSet.getDate("publishDate").toLocalDate(),
                            resultSet.getString("ISBN"),
                            resultSet.getString("publisher"),
                            resultSet.getString("genre"),
                            resultSet.getString("originalLanguage"),
                            resultSet.getString("description"),
                            resultSet.getString("thumbnail"),
                            imageView,
                            resultSet.getDouble("rate"),
                            resultSet.getInt("rateQuantities"));
                    bookList.add(currentBook);
                }
                resultSet.close();
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
            returnLoading();
        });
        dbQuery.setOnFailed(event -> {
            System.out.println("Failed");
            returnLoading();
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }
}