package com.jmc.library.Controllers.Admin;

import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class PendingController {

    /** need make assets IssueInfo or some things like BookInfo*/
    public TableView/*<IssueInfo>*/ store_tb;
    public TableColumn issue_id_tb_cl;
    public TableColumn book_id_tb_cl;
    public TableColumn customer_tb_cl;
    public TableColumn borrow_date_tb_cl;
    public TableColumn due_date_tb_cl;
    public TableColumn cost_tb_cl;
    /** This column is where select the status for issue */
    public TableColumn/*<IssueInfo, ChoiceBox>*/ status_tb_cl;

    public Button update_btn;
}
