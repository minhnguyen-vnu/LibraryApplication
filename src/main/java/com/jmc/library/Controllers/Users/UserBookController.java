package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.LibraryTable;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserBookController extends UserLibraryTable implements Initializable {
    public TableColumn<UserBookInfo, Double> total_cost_tb_cl;

    public AnchorPane user_info_pane;
    public AnchorPane matte_screen;
    public TableColumn cover_book_tb_cl;
    public TableColumn get_rate_tb_cl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListenerMaterial();
        setTable();
        addBinding();
        priceFormating();
        setButtonListener();
        showLibrary();
    }

    @Override
    protected void addBinding() {
        super.addBinding();
        total_cost_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
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
                "    r.pickedDate,\n" +
                "    r.returnDate,\n" +
                "    r.cost,\n" +
                "    r.requestStatus\n"+
                "from  userRequest r\n" +
                "join bookStore b using(bookId)\n" +
                "where r.username = ? " +
                "order by r.requestStatus;", LibraryModel.getInstance().getUser().getUsername());
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    UserBookInfo userBookInfo = new UserBookInfo(resultSet.getString("bookName"), resultSet.getString("authorName"),
                            resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                            resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"), resultSet.getString("requestStatus"));
                    bookList.add(userBookInfo);
                }
                resultSet.close();
                returnLoading();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    private void priceFormating() {
        total_cost_tb_cl.setCellFactory(new Callback<TableColumn<UserBookInfo, Double>, TableCell<UserBookInfo, Double>>() {
            @Override
            public TableCell<UserBookInfo, Double> call(TableColumn<UserBookInfo, Double> param) {
                return new TableCell<UserBookInfo, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("$" + String.format("%.2f", item));
                        }
                    }
                };
            }
        });
    }
}