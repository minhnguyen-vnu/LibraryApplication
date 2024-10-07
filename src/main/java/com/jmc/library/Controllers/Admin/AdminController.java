package com.jmc.library.Controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AdminController implements Initializable {
    public Button go_to_library_btn;
    public Button go_to_setting_btn;
    public Button search_btn;
    public Button check_out_btn;
    public TextField search_fld;
    public TableView<AdminTableItiem> store_tb;
    public TableColumn<AdminTableItiem, CheckBox> check_out_tb_cl;
    public TableColumn<AdminTableItiem, HBox> name_tb_cl;
    public TableColumn<AdminTableItiem, String> id_tb_cl;
    public TableColumn<AdminTableItiem, String> picked_day_tb_cl;
    public TableColumn<AdminTableItiem, String> return_day_tb_cl;
    public TableColumn<AdminTableItiem, String> total_cost_tb_cl;
    public TableColumn<AdminTableItiem, Button> action_tb_cl;

    public ObservableList<AdminTableItiem> adminItems;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        addAdminItemsListener();
        store_tb.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        adminItems.add(new AdminTableItiem(new CheckBox(), "/IMAGES/actionIcon.png", "The Great Gatsby", "1", "2021-10-10", "2021-10-20", "10.00", "/IMAGES/avatar.png"));
        search_btn.setOnAction(actionEvent -> searchAdminItem(search_fld.getText()));
    }

    private void addBinding() {
        adminItems = FXCollections.observableArrayList();
        store_tb.setItems(adminItems);

        check_out_tb_cl.setCellValueFactory(new PropertyValueFactory<>("checkOut"));
        check_out_tb_cl.setCellFactory(new Callback<TableColumn<AdminTableItiem, CheckBox>, TableCell<AdminTableItiem, CheckBox>>() {
            @Override
            public TableCell<AdminTableItiem, CheckBox> call(TableColumn<AdminTableItiem, CheckBox> param) {
                return new TableCell<AdminTableItiem, CheckBox>() {
                    @Override
                    protected void updateItem(CheckBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(item);
                        }
                    }
                };
            }
        });

        name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("nameHBox"));
        name_tb_cl.setCellFactory(new Callback<TableColumn<AdminTableItiem, HBox>, TableCell<AdminTableItiem, HBox>>() {
            @Override
            public TableCell<AdminTableItiem, HBox> call(TableColumn<AdminTableItiem, HBox> param) {
                return new TableCell<AdminTableItiem, HBox>() {
                    @Override
                    protected void updateItem(HBox item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(item);
                        }
                    }
                };
            }
        });

        id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("id"));
        picked_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("pickedDay"));
        return_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("returnDay"));
        total_cost_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        action_tb_cl.setCellValueFactory(new PropertyValueFactory<>("action"));
        action_tb_cl.setCellFactory(new Callback<TableColumn<AdminTableItiem, Button>, TableCell<AdminTableItiem, Button>>() {
            @Override
            public TableCell<AdminTableItiem, Button> call(TableColumn<AdminTableItiem, Button> param) {
                return new TableCell<AdminTableItiem, Button>() {
                    @Override
                    protected void updateItem(Button item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(item);
                        }
                    }
                };
            }
        });
    }

    private void addAdminItemsListener() {
        adminItems.addListener((ListChangeListener<AdminTableItiem>) change -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasRemoved()) {
                    store_tb.refresh();
                }
            }
        });
    }

    private void searchAdminItem(String text) {
        if (text.isEmpty()) {
            showAdminItems();
        } else {
            searchAdminItemByName(text);
        }
    }

    private void searchAdminItemByName(String text) {
        ObservableList<AdminTableItiem> filteredList = adminItems.stream()
                .filter(item -> item.getName().equalsIgnoreCase(text))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
        store_tb.getItems().addListener((ListChangeListener<AdminTableItiem>) change -> {
            if (!change.next() || change.wasAdded() || change.wasRemoved()) {
                filteredList.clear();
            }
        });
    }

    private void showAdminItems() {
        store_tb.setItems(adminItems);
    }
}