package com.jmc.library.Controllers.Admin;

import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.*;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class AdminDashboardController implements Initializable {
    public Button go_to_dashboard_btn;
    public Button manage_btn;
    public Button go_to_request_btn;
    public Button log_out_btn;
    public ListView trending;
    public Label view_all_lbl;
    public Label welcome_admin_lbl;
    public Label diff_lbl;
    public Label total_borrowed_book_lbl;
    public LineChart<String, Number> customers_line_chart;
    public Label total_customers_lbl;
    public Button go_to_pending_btn;
    public Button user_management_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        show();
        onAction();
    }

    private void show() {
        Thread UIThread = new Thread(() -> {
            while (true) {
                if (AdminLibraryModel.getInstance().isThread1() && AdminLibraryModel.getInstance().isThread2() && AdminLibraryModel.getInstance().isThread3()) {
                    Platform.runLater(() -> {
                        total_customers_lbl.setText(String.valueOf(AdminLibraryModel.getInstance().getTotalUsers()));
                        diff_lbl.setText(String.valueOf(AdminLibraryModel.getInstance().getTotalUsers() - AdminLibraryModel.getInstance().getTotalUser30PreviousDays()));
                        total_borrowed_book_lbl.setText(String.valueOf(AdminLibraryModel.getInstance().getTotalBorrowedBooks()));
                    });
                    break;
                }
            }
        });
        UIThread.setDaemon(true);
        UIThread.start();


        System.out.println(AdminLibraryModel.getInstance().getTotalUsers());

        customers_line_chart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Data Series 1");

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();


        Thread thread = new Thread(() -> {
            for (int i = 1; i <= currentMonth; i++) {
                int month = i;
                DBQuery dbQuery = new DBQuery("select count(*) as cnt from users where month(registeredDate) = ? and isAdmin = 0;", i);
                dbQuery.setOnSucceeded(event -> {
                    ResultSet resultSet = dbQuery.getValue();
                    try {
                        if (resultSet.next()) {
                            int num = resultSet.getInt("cnt");
                            if (num > 0) {
                                Platform.runLater(() -> {
                                    series.getData().add(new XYChart.Data<>(String.valueOf(month), num));
                                });
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            resultSet.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
                dbQuery.setOnFailed(event -> dbQuery.getException().printStackTrace());

                dbQuery.run();
            }
            Platform.runLater(() -> customers_line_chart.getData().add(series));
        });
        thread.setDaemon(true);
        thread.start();


        customers_line_chart.getData().add(series);
    }

    private void onAction() {
        manage_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View"));
        go_to_request_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Request Management"));
        go_to_pending_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Pending Request Management"));
        log_out_btn.setOnAction(actionEvent -> stageTransforming());
    }

    private void stageTransforming() {
        Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().showAuthenticationWindow();
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Login");
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("");
    }
}