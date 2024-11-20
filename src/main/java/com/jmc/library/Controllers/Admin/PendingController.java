package com.jmc.library.Controllers.Admin;

import javafx.scene.control.*;

public class PendingController {

   
    public TextField get_customer_name_txt_fld;
    public DatePicker get_borrowed_date;
    public DatePicker get_due_date;
    public ChoiceBox status_choice_box;
    public Button search_btn;
    public Button update_btn;
    public Button return_btn;
    public TableView store_tb;
    public TableColumn issue_id_cl;
    public TableColumn book_id_cl;
    public TableColumn customer_cl;
    public TableColumn borrowed_date_cl;
    public TableColumn due_date_cl;
    public TableColumn cost_cl;
    public TableColumn status_cl;
}
