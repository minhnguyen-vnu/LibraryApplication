package com.jmc.library.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;

public class HiredBookController {
    public DatePicker clock;
    // use <> to specify the type of the items in the TableView. For example, TableView<Book> means that the TableView will contain Book objects
    public TableView Store_tbv;
    // use <> to specify the type of the items in the TableColumn. For example, TableColumn<Book, String> means that the TableColumn will contain String objects
    public TableColumn Book_name_tb_cl;
    public TableColumn Author_tb_cl;
    public TableColumn Book_ID_tb_cl;
    public TableColumn Borrow_day_tb_cl;
    public TableColumn Favorite_tb_cl;
    public TableColumn Return_day_tb_cl;
    public Label username_lbl;
    public Button go_to_setting_btn;
    public Button go_to_library_btn;

    @FXML
    private TextField search_fld;
    @FXML
    private Button search_btn;


    public void initialize() {
        clock.setValue(LocalDate.now());
    }
}