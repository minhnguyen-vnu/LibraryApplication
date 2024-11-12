package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Interface.DashboardUpdateListener;
import com.jmc.library.Controllers.Interface.InterfaceManager;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * this scene is the user dashboard
 * it not complete yet
 * now every thing is not designed
 */

public class UserDashboardController extends User implements Initializable, DashboardUpdateListener {

    public Button go_to_library_btn;
    public Button go_to_store_btn;
    public Button go_to_setting_btn;
    public Button log_out_btn;
    public Button search_btn;

    public Label username_lbl;
    public Label welcome_username_lbl;
    public Label view_all_lbl;
    public Label total_read_book_lbl;
    public Label new_books_read_lbl;
    public Label total_hired_book_lbl;

    public ImageView account_avatar_img;

    public TextField search_fld;

    public ListView<UserBookInfo> hot_book_list;

    public LineChart<String, Number> over_view_line_chart;
    public NumberAxis book_borrowed_over_view_na;
    public CategoryAxis month_over_view_ca;

    public BarChart<String, Number> read_and_hired_bar_chart;
    public NumberAxis read_hired_bar_chart_na;
    public CategoryAxis month_total_read_hired_ca;

    public ObservableList<UserBookInfo> bookList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        InterfaceManager.getInstance().setDashboardUpdateListener(this);
        addBinding();
        setButtonListener();
        setAnotherListener();
    }

    @Override
    public void onTotalReadBookUpdate(int totalReadBook) {
        total_read_book_lbl.setText(String.valueOf(totalReadBook));
    }

    @Override
    public void onNewBooksReadUpdate(int newBooksRead) {
        new_books_read_lbl.setText(String.valueOf(newBooksRead));
    }

    // thieu pending/cart/ + bo search + bo setting
    private void setButtonListener() {
        search_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });

        go_to_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });

        go_to_store_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        go_to_setting_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("Setting");
        });

        log_out_btn.setOnAction(actionEvent -> {
            Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
            Model.getInstance().getViewFactory().closeStage(currentStage);
            Model.getInstance().getViewFactory().showAuthenticationWindow();
        });
    }

    private void setAnotherListener() {
        //lam sau
        view_all_lbl.setOnMouseClicked(mouseEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        welcome_username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
        username_lbl.setText(LibraryModel.getInstance().getUser().getUsername());
        account_avatar_img.setImage(new ImageView(getClass().getResource("/IMAGES/avatar.png").toExternalForm()).getImage());
        /*
        total_read_book_lbl.setText(String.valueOf
                (LibraryModel.getInstance().getUser().
                        getBookHiredList().size()));

        
        total_hired_book_lbl.setText(String.valueOf(bookList.size()));
        */
    }

    private void addBinding() {
        hot_book_list.setItems(bookList);
        System.err.println("UserDashboardController.addBinding");
        setOverViewLineChart();
        setReadAndHiredBarChart();
    }

    private void setOverViewLineChart() {

        over_view_line_chart.setTitle("Book Borrowed Overview");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Book Borrowed");
        //de tam thoi sau nay se sua lai
        series.getData().add(new XYChart.Data<>("Jan", 100));
        series.getData().add(new XYChart.Data<>("Feb", 200));
        series.getData().add(new XYChart.Data<>("Mar", 300));
        series.getData().add(new XYChart.Data<>("Apr", 400));

        over_view_line_chart.getData().add(series);
    }

    private void setReadAndHiredBarChart() {
        read_and_hired_bar_chart.setTitle("Read and Hired Overview");
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName("Read");
        //de tam thoi sau nay se sua lai
        series1.getData().add(new XYChart.Data<>("Jan", 100));
        series1.getData().add(new XYChart.Data<>("Feb", 200));
        series1.getData().add(new XYChart.Data<>("Mar", 300));
        series1.getData().add(new XYChart.Data<>("Apr", 400));

        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Hired");
        //de tam thoi sau nay se sua lai
        series2.getData().add(new XYChart.Data<>("Jan", 500));
        series2.getData().add(new XYChart.Data<>("Feb", 600));
        series2.getData().add(new XYChart.Data<>("Mar", 700));
        series2.getData().add(new XYChart.Data<>("Apr", 800));

        read_and_hired_bar_chart.getData().addAll(series1, series2);
    }
}
