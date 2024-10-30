package com.jmc.library.Controllers.Admin;

import com.jmc.library.DBUtlis;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminDashboardController implements Initializable {
    public Button go_to_dashboard_btn;
    public Button manage_btn;
    public Button go_to_setting_btn;
    public Button log_out_btn;
    public ListView trending;
    public Label view_all_lbl;
    public Label welcome_admin_lbl;
    public Label diff_lbl;
    public Label total_hired_book_lbl;
    public LineChart<String, Number> customers_line_chart;
    public Label total_customers_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        show();
        onAction();
    }

    private void show() {
        total_customers_lbl.setText(String.valueOf(AdminLibraryModel.getInstance().getTotalUsers()));
        diff_lbl.setText(String.valueOf(AdminLibraryModel.getInstance().getTotalUsers() - AdminLibraryModel.getInstance().getTotalUser30PreviousDays()));
        total_hired_book_lbl.setText(String.valueOf(AdminLibraryModel.getInstance().getTotalHiredBooks()));

        // Ensure the LineChart is not re-initialized
        customers_line_chart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Data Series 1");

        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        boolean ok = false;
        for (int i = 1; i <= currentMonth; i++) {
            try {
                ResultSet resultSet = DBUtlis.executeQuery("select count(*) as cnt from users where month(registeredDate) = ? and isAdmin = 0;", i);
                if (resultSet.next()) {
                    int num = resultSet.getInt("cnt");
                    if (num > 0) ok = true;
                    if (ok) {
                        System.out.println(i + " " + num);
                        series.getData().add(new XYChart.Data<>(String.valueOf(i), num));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        customers_line_chart.getData().add(series);
    }

    private void onAction() {
        manage_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View");
        });
    }
}