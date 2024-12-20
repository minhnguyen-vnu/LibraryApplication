package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Assets.RatingController;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.LibraryModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller class for managing user books, including displaying and rating books.
 */
public class UserBookController extends UserLibraryTable implements Initializable {
    public TableColumn<UserBookInfo, Double> total_cost_tb_cl;

    public AnchorPane user_info_pane;
    public AnchorPane matte_screen;
    public TableColumn<UserBookInfo, CheckBox> get_rate_tb_cl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListenerMaterial();
        setTable();
        addBinding();
        priceFormating();
        setButtonListener();
    }

    @Override
    protected void addBinding() {
        super.addBinding();
        total_cost_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalCost"));
        get_rate_tb_cl.setCellValueFactory(new PropertyValueFactory<>("Rate"));
    }


    @Override
    protected void setTable() {
        returnLoading();
        bookList = LibraryModel.getInstance().getUser().getBorrowedBookList();
    }

    @Override
    protected void showLibrary() {
        addLoading();
        bookList.clear();
        DBQuery dbQuery = new DBQuery("select\n" +
                "    r.username,\n" +
                "    r.bookName,\n" +
                "    b.authorName,\n" +
                "    r.bookId,\n" +
                "    r.pickedDate,\n" +
                "    r.returnDate,\n" +
                "    r.cost,\n" +
                "    r.requestStatus,\n" +
                "    r.isRated, \n" +
                "    b.imageView\n" +
                "from  userRequest r\n" +
                "join bookStore b using(bookId)\n" +
                "where r.username = ? and r.requestStatus = 'Borrowing'" +
                "order by r.requestStatus;", LibraryModel.getInstance().getUser().getUsername());
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
                            resultSet.getInt("bookId"), resultSet.getDate("pickedDate").toLocalDate(),
                            resultSet.getDate("returnDate").toLocalDate(), resultSet.getDouble("cost"), resultSet.getString("requestStatus"), resultSet.getBoolean("isRated"), imageView);
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

    /**
     * Formats the price column in the table.
     */
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

        get_rate_tb_cl.setCellValueFactory(param -> {
            UserBookInfo bookInfo = param.getValue();
            CheckBox checkBox = new CheckBox();
            checkBox.setSelected(bookInfo.getRate());
            checkBox.setDisable(bookInfo.getRate());
            checkBox.setOnAction(event -> {
                if (!bookInfo.getRate()) {
                    showRatingDialog(bookInfo, checkBox);
                }
            });
            return new SimpleObjectProperty<>(checkBox);
        });

        get_rate_tb_cl.setCellFactory(new Callback<TableColumn<UserBookInfo, CheckBox>, TableCell<UserBookInfo, CheckBox>>() {
            @Override
            public TableCell<UserBookInfo, CheckBox> call(TableColumn<UserBookInfo, CheckBox> param) {
                return new TableCell<UserBookInfo, CheckBox>() {
                    private final CheckBox checkBox = new CheckBox();

                    @Override
                    protected void updateItem(CheckBox item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || getIndex() >= getTableView().getItems().size()) {
                            setGraphic(null);
                            return;
                        }

                        UserBookInfo bookInfo = getTableView().getItems().get(getIndex());

                        checkBox.setSelected(bookInfo.getRate());
                        checkBox.setDisable(bookInfo.getRate());
                        setGraphic(checkBox);

                        checkBox.setOnAction(event -> {
                            if (!bookInfo.getRate()) {
                                showRatingDialog(bookInfo, checkBox);
                            }
                        });
                    }
                };
            }
        });

    }

    private boolean showRatingDialog(UserBookInfo bookInfo,CheckBox checkBox) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/RateStar.fxml"));
            Parent ratingRoot = loader.load();
            RatingController ratingController = loader.getController();
            ratingController.setUserBookInfo(bookInfo);
            ratingController.setCheckBox(checkBox);
            Stage ratingStage = new Stage();
            ratingStage.setTitle("Rate the Book");
            ratingStage.setScene(new Scene(ratingRoot));
            ratingStage.initModality(Modality.APPLICATION_MODAL);
            ratingStage.show();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}