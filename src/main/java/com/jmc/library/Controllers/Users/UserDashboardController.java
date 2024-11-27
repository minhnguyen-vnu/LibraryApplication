package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Database.*;
import com.jmc.library.Models.DashboardModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.beans.property.ListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.ResourceBundle;

public class UserDashboardController extends com.jmc.library.Controllers.Users.User implements Initializable {

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
    public Label total_hired_book_lbl;

    public ListView<String> hot_book_list;

    public List<Integer> borrowedBooks;
    public List<Integer> readBooks;

    public LineChart<String, Number> over_view_line_chart;
    public NumberAxis book_borrowed_over_view_na;
    public CategoryAxis month_over_view_ca;

    public BarChart<String, Number> read_and_hired_bar_chart;
    public NumberAxis read_hired_bar_chart_na;
    public CategoryAxis month_total_read_hired_ca;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        setButtonListener();
        setMaterialListener();
    }

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

    private void stageTransforming() {
        Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().showAuthenticationWindow();
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Login");
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("");
    }

    private void setMaterialListener() {
        view_all_lbl.setOnMouseClicked(mouseEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        welcome_username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
    }

    private void setHotBookList() {
        DBQuery dbQuery = new DBQuery("select bookName from bookStore order by quantityInStock DESC limit 3;");
        dbQuery.setOnSucceeded(event -> {
            try {
                ResultSet resultSet = dbQuery.getValue();
                while (resultSet.next()) {
                    hot_book_list.getItems().add(resultSet.getString("bookName"));
                }
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }

    private void addBinding() {
        setHotBookList();
        total_read_book_lbl.setText(String.valueOf(DashboardModel.getInstance().getUserDashboardInfo().getCountReadBook()));
        total_hired_book_lbl.setText(String.valueOf(DashboardModel.getInstance().getUserDashboardInfo().getCountHiredBook()));
        new_books_read_lbl.setText(String.valueOf(DashboardModel.getInstance().getUserDashboardInfo().getCountNewReadBook()));

        borrowedBooks = DashboardModel.getInstance().getUserDashboardInfo().getBorrowedBooks();
        readBooks = DashboardModel.getInstance().getUserDashboardInfo().getReadBooks();
        setChart();

        DashboardModel.getInstance().getUserDashboardInfo().getTotal_hired_book_lbl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
                total_read_book_lbl.setText(newVal);
                total_hired_book_lbl.setText(newVal);
                new_books_read_lbl.setText(newVal);

                welcome_username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
            }
        });

        DashboardModel.getInstance().getUserDashboardInfo().borrowedBooksProperty().addListener((observableValue, oldValue, newValue) -> {
            borrowedBooks = newValue;
            total_hired_book_lbl.setText(String.valueOf(DashboardModel.getInstance().getUserDashboardInfo().getCountHiredBook()));
            if (borrowedBooks.size() == 12) {
                setChart();
            }
        });

        DashboardModel.getInstance().getUserDashboardInfo().readBooksProperty().addListener((observableValue, oldValue, newValue) -> {
            readBooks = newValue;
            total_read_book_lbl.setText(String.valueOf(DashboardModel.getInstance().getUserDashboardInfo().getCountReadBook()));
            if (borrowedBooks.size() == 12) {
                setChart();
            }
        });
    }

    private void setChart() {
        clearChart();
        setOverViewLineChart();
        setReadAndHiredBarChart();
    }

    private void clearChart() {
        over_view_line_chart.getData().clear();
        read_and_hired_bar_chart.getData().clear();
    }

    private void setOverViewLineChart() {
        over_view_line_chart.setTitle("Book Borrowed Overview");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Book Borrowed");
        int startMonth = LocalDate.now().getMonthValue() + 1;
        for(int i = startMonth; i < startMonth + 12; i ++) {
            series.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(), borrowedBooks.get((i - 1) % 12)));
        }
        over_view_line_chart.getData().add(series);
    }

    private void setReadAndHiredBarChart() {
        read_and_hired_bar_chart.setTitle("Read and Hired Overview");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Read");

        int startMonth = LocalDate.now().getMonthValue() + 1;
        for(int i = startMonth; i < startMonth + 12; i ++) {
            series1.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(), readBooks.get((i - 1) % 12)));
        }

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Hired");
        for(int i = startMonth; i < startMonth + 12; i ++) {
            series2.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(), borrowedBooks.get((i - 1) % 12)));
        }
        read_and_hired_bar_chart.getData().addAll(series1, series2);
    }
}
