package com.jmc.library.Controllers.Admin;

import javafx.scene.control.*;

public class ManageUserController {
    public TextField username_txt_fld;
    public TextField full_name_txt_fld;
    public DatePicker date_of_birth_date_picker;
    public ChoiceBox status_choice_box;
    public Button search_btn;
    public Button return_btn;
    public Button reload_btn;

    public TableView store_tb;
    public TableColumn username_tb_cl;
    public TableColumn full_name_tb_cl;
    public TableColumn date_of_birth_tb_cl;
    public TableColumn total_paid_tb_cl;
    public TableColumn total_issue_tb_cl;
    public TableColumn status_tb_cl;
}
