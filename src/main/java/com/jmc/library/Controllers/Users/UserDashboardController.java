package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Interface.DashboardUpdateListener;
import com.jmc.library.Controllers.Interface.InterfaceManager;
import com.jmc.library.Database.*;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
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
import java.time.LocalDate;
import java.time.Month;
import java.util.ResourceBundle;


/**
 * this scene is the user dashboard
 * it not complete yet
 * now every thing is not designed
 */

public class UserDashboardController extends com.jmc.library.Controllers.Users.User implements Initializable, DashboardUpdateListener {

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

    public ListView<Pair<String, String>> hot_book_list;

    public LineChart<String, Number> over_view_line_chart;
    public NumberAxis book_borrowed_over_view_na;
    public CategoryAxis month_over_view_ca;

    public BarChart<String, Number> read_and_hired_bar_chart;
    public NumberAxis read_hired_bar_chart_na;
    public CategoryAxis month_total_read_hired_ca;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InterfaceManager.getInstance().setDashboardUpdateListener(this);
        addBinding();
        setButtonListener();
        setMaterialListener();
    }

    @Override
    public void onDashBoardUpdate() {
        ObservableList<UserBookInfo> bookList = LibraryModel
                .getInstance().getUser().getBookHiredList();
        int countReadBook = 0;
        int countHiredBook = 0;
        int countNewReadBook = 0;
        for (UserBookInfo book : bookList) {
            if (book.getReturnDate().isBefore(LocalDate.now())) {
                countReadBook ++;
                if(book.getReturnDate().isBefore(LocalDate.now().minusDays(7))) {
                    countNewReadBook ++;
                }
            }
            countHiredBook ++;
        }
        total_read_book_lbl.setText(String.valueOf(countReadBook));
        new_books_read_lbl.setText(String.valueOf(countNewReadBook));
        total_hired_book_lbl.setText(String.valueOf(countHiredBook));
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
            LibraryModel.getInstance().getUser().resetAll();
            Model.getInstance().getViewFactory().resetAll();
            Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
            Model.getInstance().getViewFactory().showAuthenticationWindow();
        });
    }

    private void setMaterialListener() {
        //lam sau
        view_all_lbl.setOnMouseClicked(mouseEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        welcome_username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
    }

    private void setHotBookList() {
        try {
            ResultSet resultSet = DBUtlis.executeQuery("select bookName, " +
                    "authorName from bookStore order by quantityInStock DESC limit 3;");
            while (resultSet.next()) {
                hot_book_list.getItems().add(new Pair<>(resultSet.getString("bookName"),
                        resultSet.getString("authorName")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addBinding() {
        setHotBookList();
        System.err.println("UserDashboardController.addBinding");
        onDashBoardUpdate();
        setOverViewLineChart();
        setReadAndHiredBarChart();
    }

    private void setOverViewLineChart() {
        over_view_line_chart.setTitle("Book Borrowed Overview");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Book Borrowed");
        int[] borrowedBook = new int[12];
        for (UserBookInfo book : LibraryModel.getInstance().getUser().getBookHiredList()) {
            if (book.getPickedDate().getYear() == LocalDate.now().getYear()
                    || (book.getPickedDate().getYear() == LocalDate.now().getYear()
                    && book.getPickedDate().getMonthValue() >= LocalDate.now().getMonthValue())) {
                borrowedBook[book.getPickedDate().getMonthValue() - 1] ++;
            }
        }
        int startMonth = LocalDate.now().getMonthValue() + 1;
        for(int i = startMonth; i < startMonth + 12; i ++) {
            series.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(), borrowedBook[(i - 1) % 12]));
        }
        over_view_line_chart.getData().add(series);
    }

    private void setReadAndHiredBarChart() {
        read_and_hired_bar_chart.setTitle("Read and Hired Overview");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Read");

        int[] readBook = new int[12];
        for (UserBookInfo book : LibraryModel.getInstance().getUser().getBookHiredList()) {
            if (book.getReturnDate().getYear() == LocalDate.now().getYear()
                    || (book.getReturnDate().getYear() == LocalDate.now().getYear()
                    && book.getReturnDate().getMonthValue() >= LocalDate.now().getMonthValue())) {
                if (book.getReturnDate().isBefore(LocalDate.now())) {
                    readBook[book.getReturnDate().getMonthValue() - 1] ++;
                }
            }
        }
        int startMonth = LocalDate.now().getMonthValue() + 1;
        for(int i = startMonth; i < startMonth + 12; i ++) {
            series1.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(), readBook[(i - 1) % 12]));
        }

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Hired");
        int[] hiredBook = new int[12];
        for (UserBookInfo book : LibraryModel.getInstance().getUser().getBookHiredList()) {
            if (book.getPickedDate().getYear() == LocalDate.now().getYear()
                    || (book.getPickedDate().getYear() == LocalDate.now().getYear()
                    && book.getPickedDate().getMonthValue() >= LocalDate.now().getMonthValue())) {
                hiredBook[book.getPickedDate().getMonthValue() - 1] ++;
            }
        }
        for(int i = startMonth; i < startMonth + 12; i ++) {
            series2.getData().add(new XYChart.Data<>(Month.of((i - 1) % 12 + 1).toString(), hiredBook[(i - 1) % 12]));
        }

        read_and_hired_bar_chart.getData().addAll(series1, series2);
    }
}
