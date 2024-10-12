package com.jmc.library.Controllers.Users;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;


/**
 * this scene is the user dashboard
 * it not complete yet
 * now every thing is not designed
 */

public class UserDashboardController {

    public Button go_to_library_btn;
    public Button go_to_store_btn;
    public Button go_to_setting_btn;
    public Button log_out_btn;
    public Label username_lbl;
    public ImageView account_avatar_img;
    public Button search_btn;
    public TextField search_fld;
    public Label welcome_username_lbl;
    /**
     * hot_book_list: list view of hot books
     * type: ListView
     */
    public ListView hot_book_list;
    /**
     * view_all_lbl: link to view all books in hot book list
     * but when clicked, it will redirect to the store page
     */
    public Label view_all_lbl;
    /**
     * total_read_book_lbl: label to show total read books
     * just a number
     */
    public Label total_read_book_lbl;
    /**
     * new_books_read_lbl: label to show new books read in the last 7 days
     * just a number
     */
    public Label new_books_read_lbl;
    /**
     * total_hired_book_lbl: label to show total hired books
     * just a number
     */
    public Label total_hired_book_lbl;
    /**
     * over_view_line_chart: line chart to show the overview of the user's reading and hiring activities
     * type: LineChart
     */
    public LineChart over_view_line_chart;
    /**
     * read_and_hired_bar_chart: bar chart to show the user's reading and hiring activities
     * show the total read books and total hired books per month
     * type: BarChart
     */
    public BarChart read_and_hired_bar_chart;
}
