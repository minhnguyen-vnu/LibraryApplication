package com.jmc.library.Controllers.Users;

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

public class HiredBookController implements Initializable {
    public Button go_to_library_btn;
    public Button go_to_setting_btn;
    public Label username_lbl;
    public TableView<UserBook> Store_tbv;
    public TableColumn<UserBook, String> book_name_tb_cl;
    public TableColumn<UserBook, String> author_tb_cl;
    public TableColumn<UserBook, Integer> book_ID_tb_cl;
    public TableColumn<UserBook, LocalDate> return_day_tb_cl;
    public TableColumn<UserBook, LocalDate> picked_day_tb_cl;
    public DatePicker clock;
    public TextField search_fld;
    public Button search_btn;
    public ObservableList<UserBook> bookList;
    public TableColumn<UserBook, Double> total_cost_tb_cl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBinding();
        priceFormating();

        bookList.add(new UserBook("Java Programing", "Scott", 1, LocalDate.now(), LocalDate.of(2024, 6, 7), 13));
        bookList.add(new UserBook("C++ Programing", "Scoot", 2, LocalDate.now(), LocalDate.of(2024, 1, 1), 15));

        search_btn.setOnAction(actionEvent -> searchBookByAuthor(search_fld.getText()));
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
        total_cost_tb_cl.setCellFactory(new Callback<TableColumn<UserBook, Double>, TableCell<UserBook, Double>>() {
            @Override
            public TableCell<UserBook, Double> call(TableColumn<UserBook, Double> param) {
                return new TableCell<UserBook, Double>() {
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
        ObservableList<UserBook> filteredList = bookList.stream()
                .filter(book -> book.getAuthorName().equalsIgnoreCase(authorName))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        Store_tbv.setItems(filteredList);
    }

}