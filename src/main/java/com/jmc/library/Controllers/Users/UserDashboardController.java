package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.*;
import com.jmc.library.Models.DashboardModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import com.jmc.library.Models.TopBookModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for managing the user dashboard, including statistics and navigation.
 */
public class UserDashboardController implements Initializable {


    public Button go_to_library_btn;
    public Button go_to_store_btn;
    public Button go_to_setting_btn;
    public Button go_to_dashboard_btn;
    public Button go_to_cart_btn;
    public Button go_to_pending_btn;
    public Button log_out_btn;

    public Label welcome_username_lbl;
    public Label view_all_lbl;
    public Label total_read_book_lbl;
    public Label new_books_read_lbl;
    public Label total_borrowed_book_lbl;

    public ListView<String> hot_book_list;

    public List<Integer> borrowedBooks;
    public List<Integer> readBooks;

    public LineChart<String, Number> over_view_line_chart;
    public NumberAxis book_borrowed_over_view_na;
    public CategoryAxis month_over_view_ca;

    public BarChart<String, Number> read_and_borrowed_bar_chart;
    public NumberAxis read_borrowed_bar_chart_na;
    public CategoryAxis month_total_read_borrowed_ca;
    public Button reload_btn;
    public ImageView most_liked_book;
    public ImageView loading_img;
    public VBox place_holder;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addLoading();
        addBinding();
        topBookSuggestion();
        setButtonListener();
        setMaterialListener();
    }

    public void addLoading() {
        if(loading_img == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Loading.fxml"));
            try {
                loading_img = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        place_holder.setVisible(true);
        place_holder.getChildren().add(loading_img);
    }

    void returnLoading() {
        place_holder.getChildren().remove(loading_img);
        place_holder.setVisible(false);
    }

    /**
     * Sets the initial values for the user information fields.
     */
    private void setMaterialListener() {
        view_all_lbl.setOnMouseClicked(mouseEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("Top Rated Books");
        });

        welcome_username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
    }

    /**
     * Sets the button listeners for navigation and actions.
     */
    private void setButtonListener() {
        go_to_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });

        go_to_store_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        go_to_cart_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Cart");
        });

        go_to_pending_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });

        go_to_setting_btn.setOnAction(actionEvent -> {
            Scene currentScene = go_to_setting_btn.getScene();
            try {
                UserInfoOverlay userInfoOverlay = new UserInfoOverlay(currentScene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        reload_btn.setOnAction(event -> {
            onDashboardUpdate();
        });

        log_out_btn.setOnAction(actionEvent -> {
            DBUpdate dbUpdate = new DBUpdate("update users\n" +
                    "set status = ?\n" +
                    "where username = ?;", "offline", LibraryModel.getInstance().getUser().getUsername());
            Thread thread = new Thread(dbUpdate);
            thread.setDaemon(true);
            thread.start();
            LibraryModel.getInstance().getUser().resetAll();
            stageTransforming();
        });
    }

    /**
     * Updates the dashboard with the latest data.
     */
    public void onDashboardUpdate() {
        DBQuery dbQuery = new DBQuery("select pickedDate from userRequest where userName = ?",
                LibraryModel.getInstance().getUser().getUsername());
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            int countBorrowedBook = 0;

            for (int i = 0; i < 12; i++) {
                borrowedBooks.set(i, 0);
            }
            try {
                while (resultSet.next()) {
                    LocalDate pickedDate = resultSet.getDate("pickedDate").toLocalDate();
                    int month = pickedDate.getMonthValue() - 1;
                    borrowedBooks.set(month, borrowedBooks.get(month) + 1);
                    countBorrowedBook++;
                }
                resultSet.close();
                DashboardModel.getInstance().getUserDashboardInfo().setBorrowedBooks(
                        (ObservableList<Integer>) borrowedBooks);
                DashboardModel.getInstance().getUserDashboardInfo().setCountBorrowedBook(countBorrowedBook);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     * Transforms the stage to the authentication window.
     */
    private void stageTransforming() {
        Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().showAuthenticationWindow();
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Login");
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("");
    }

    /**
     * Adds bindings to the UI components.
     */
    private void addBinding() {
        total_read_book_lbl.setText(String.valueOf(
                DashboardModel.getInstance().getUserDashboardInfo().getCountReadBook()));
        total_borrowed_book_lbl.setText(String.valueOf(
                DashboardModel.getInstance().getUserDashboardInfo().getCountBorrowedBook()));
        new_books_read_lbl.setText(String.valueOf(
                DashboardModel.getInstance().getUserDashboardInfo().getCountNewReadBook()));

        borrowedBooks = DashboardModel.getInstance().getUserDashboardInfo().getBorrowedBooks();
        readBooks = DashboardModel.getInstance().getUserDashboardInfo().getReadBooks();
        setChart();

        DashboardModel.getInstance().getUserDashboardInfo().getTotal_borrowed_book_lbl().textProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String oldVal,
                                        String newVal) {
                        total_read_book_lbl.setText(newVal);
                        total_borrowed_book_lbl.setText(newVal);
                        new_books_read_lbl.setText(newVal);
                        System.out.println("Total Borrowed book: " + newVal);
                        welcome_username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
                    }
                });

        DashboardModel.getInstance().getUserDashboardInfo().borrowedBooksProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    borrowedBooks = newValue;
                    total_borrowed_book_lbl.setText(String.valueOf(
                            DashboardModel.getInstance().getUserDashboardInfo().getCountBorrowedBook()));
                    if (borrowedBooks.size() == 12) {
                        setChart();
                    }
                });

        DashboardModel.getInstance().getUserDashboardInfo().readBooksProperty().addListener(
                (observableValue, oldValue, newValue) -> {
                    readBooks = newValue;
                    total_read_book_lbl.setText(String.valueOf(
                            DashboardModel.getInstance().getUserDashboardInfo().getCountReadBook()));
                    if (borrowedBooks.size() == 12) {
                        setChart();
                    }
                });
    }

    /**
     * Sets the data for the charts.
     */
    private void setChart() {
        clearChart();
        setOverViewLineChart();
        setReadAndBorrowedBarChart();
    }

    /**
     * Clears the data from the charts.
     */
    private void clearChart() {
        over_view_line_chart.getData().clear();
        read_and_borrowed_bar_chart.getData().clear();
    }

    /**
     * Sets the data for the overview line chart.
     */
    private void setOverViewLineChart() {
        over_view_line_chart.setTitle("Book Borrowed Overview");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Book Borrowed");
        int startMonth = LocalDate.now().getMonthValue() + 1;
        for (int i = startMonth; i < startMonth + 12; i++) {
            series.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(),
                    borrowedBooks.get((i - 1) % 12)));
        }
        over_view_line_chart.getData().add(series);
    }

    /**
     * Sets the data for the read and Borrowed bar chart.
     */
    private void setReadAndBorrowedBarChart() {
        read_and_borrowed_bar_chart.setTitle("Read and Borrowed Overview");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Read");

        int startMonth = LocalDate.now().getMonthValue() + 1;
        for (int i = startMonth; i < startMonth + 12; i++) {
            series1.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(),
                    readBooks.get((i - 1) % 12)));
        }

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Borrowed");
        for (int i = startMonth; i < startMonth + 12; i++) {
            series2.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(),
                    borrowedBooks.get((i - 1) % 12)));
        }
        read_and_borrowed_bar_chart.getData().addAll(series1, series2);
    }

    /**
     * Sets the list of hot books.
     */
    private void topBookSuggestion() {
        DBQuery dbQuery = new DBQuery("select * from bookStore\n" +
                "order by rate desc\n" +
                "limit 10;");
        dbQuery.setOnSucceeded(event -> {
            try {
                ResultSet resultSet = dbQuery.getValue();
                while (resultSet.next()) {
                    Blob blob = resultSet.getBlob("imageView");
                    byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                    Image image = ImageUtils.byteArrayToImage(imageBytes);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(120);
                    imageView.setFitWidth(130);
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
                    TopBookModel.getInstance().getTopBookList().add(currentBook);
                }
                resultSet.close();
                most_liked_book.setImage(TopBookModel.getInstance().getTopBookList().getFirst().getImageView().getImage());
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