package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserPendingController extends UserLibraryTable implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListenerMaterial();
        setTable();
        addBinding();
        setButtonListener();
        showLibrary();
    }

    @Override
    protected void showLibrary() {
        addLoading();
        bookList.clear();
        store_tb.setItems(bookList);
        DBQuery dbQuery = new DBQuery("select\n" +
                "    r.username,\n" +
                "    r.bookName,\n" +
                "    b.authorName,\n" +
                "    r.bookId,\n" +
                "    r.requestDate,\n" +
                "    r.returnDate,\n" +
                "    r.cost,\n" +
                "    r.requestStatus,\n"+
                "    b.imageView\n" +
                "from PendingRequest r\n" +
                "join bookStore b using(bookId)\n" +
                "where r.username = ? order by r.requestStatus;", LibraryModel.getInstance().getUser().getUsername());
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
                    UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                            resultSet.getInt("bookId"), resultSet.getDate("requestDate").toLocalDate(),
                            resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"), resultSet.getString("requestStatus"),
                            imageView);
                    bookList.add(userBookInfo);
                }
                resultSet.close();
                returnLoading();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }
}