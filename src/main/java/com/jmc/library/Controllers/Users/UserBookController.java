package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserBookController extends User implements Initializable {
    public Button go_to_setting_btn;
    public Label username_lbl;
    public TableView<UserBookInfo> Store_tbv;
    public TableColumn<UserBookInfo, String> book_name_tb_cl;
    public TableColumn<UserBookInfo, String> author_tb_cl;
    public TableColumn<UserBookInfo, Integer> book_ID_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> return_day_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> picked_day_tb_cl;
    public DatePicker clock;
    public TextField search_fld;
    public Button search_btn;
    public ObservableList<UserBookInfo> bookList;
    public TableColumn<UserBookInfo, Double> total_cost_tb_cl;
    public Button go_to_store_btn;

    public UserBookController(String username, String token) {
        super(username, token);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        priceFormating();
        setButtonListener();

        bookList.add(new UserBookInfo("Java Programing", "Scott", 1, LocalDate.now(), LocalDate.of(2024, 6, 7), 13));
        bookList.add(new UserBookInfo("C++ Programing", "Scoot", 2, LocalDate.now(), LocalDate.of(2024, 1, 1), 15));

    }

    private void setButtonListener() {
        search_btn.setOnAction(actionEvent -> searchBookByAuthor(search_fld.getText()));
        go_to_store_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("Store");
            LibraryModel.getInstance().getLibraryController().receiveRequest(getUsername(), getToken());
        });
    }

    private void addBinding() {
        bookList = FXCollections.observableArrayList();

        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        book_ID_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        picked_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("pickedDate"));
        return_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        total_cost_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalCost"));

        Store_tbv.setItems(bookList);
    }

    private void priceFormating() {
        total_cost_tb_cl.setCellFactory(new Callback<TableColumn<UserBookInfo, Double>, TableCell<UserBookInfo, Double>>() {
            @Override
            public TableCell<UserBookInfo, Double> call(TableColumn<UserBookInfo, Double> param) {
                return new TableCell<UserBookInfo, Double>() {
                    @Override
                    protected void updateItem(Double item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText("$" + String.format("%.2f", item));
                        }
                    }
                };
            }
        });
    }

    private void searchBookByAuthor(String authorName) {
        ObservableList<UserBookInfo> filteredList = bookList.stream()
                .filter(book -> book.getAuthorName().equalsIgnoreCase(authorName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        Store_tbv.setItems(filteredList);
        filteredList.clear();
    }

}