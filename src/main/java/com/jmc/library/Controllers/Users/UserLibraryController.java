package com.jmc.library.Controllers.Users;

import com.jmc.library.DBUtlis;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class UserLibraryController implements Initializable {
    public Button go_to_store_btn;
    public Button go_to_setting_btn;
    public DatePicker clock;
    public TextField search_fld;
    public Button search_btn;
    public ImageView search_bar_btn;
    public TableColumn<BookInfo, Integer> book_id_tb_cl;
    public TableColumn<BookInfo, String> book_name_tb_cl;
    public TableColumn<BookInfo, String> author_tb_cl;
    public TableColumn<BookInfo, Integer> quantity_tb_cl;
    public TableColumn<BookInfo, Double> least_price_tb_cl;
    public TableColumn<BookInfo, Button> hire_tb_cl;
    public TableColumn<BookInfo, LocalDate> published_date_tb_cl;
    public TableView<BookInfo> store_tb;
    public ObservableList<BookInfo> bookList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        formatting();
        showLibrary();

    }

    private void addBinding(){
        bookList = FXCollections.observableArrayList();
        store_tb.setItems(bookList);

        book_id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        quantity_tb_cl.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        least_price_tb_cl.setCellValueFactory(new PropertyValueFactory<>("leastPrice"));
        published_date_tb_cl.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
    }

    private void formatting(){
        hire_tb_cl.setCellFactory(param -> new TableCell<>() {
            private final Button hireButton = new Button("Hire");

            @Override
            protected void updateItem(Button item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(hireButton);
                    hireButton.setOnAction(event -> {
                        BookInfo book = getTableView().getItems().get(getIndex());
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;
                        Connection con = DBUtlis.getConnection();
                        try{
                            preparedStatement = con.prepareStatement("update bookStore\n" +
                                    "set quantityInStock = quantityInStock - 1\n" +
                                    "where bookId = ?;");
                            preparedStatement.setInt(1, book.getBookId());
                            preparedStatement.executeUpdate();
                            showLibrary();
                        }catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        });
    }

    private void showLibrary(){
        bookList.clear();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection con = DBUtlis.getConnection();
        try {
            preparedStatement = con.prepareStatement("select * from bookStore");
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                BookInfo currentBook = new BookInfo(resultSet.getInt("bookId"), resultSet.getString("bookName"),
                        resultSet.getString("authorName"), resultSet.getInt("quantityInStock"), resultSet.getInt("leastPrice"),
                        resultSet.getDate("publishDate").toLocalDate());
                bookList.add(currentBook);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}