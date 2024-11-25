package com.jmc.library.Controllers.LibraryControllers;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.LibraryTable;
import com.jmc.library.Models.BookViewingModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LibraryController extends LibraryTable {
    public TextField search_fld;
    public Button search_btn;
    public TableColumn<BookInfo, ImageView> book_cover_tb_cl;

    protected void initialAction(){
        search_btn.setOnAction(actionEvent -> searchBook(search_fld.getText()));
        search_fld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBook(search_fld.getText());
            }
        });
    }

    private void searchBook(String text) {
        if (text.isEmpty()) {
            store_tb.setItems(bookList);
        } else {
            if (text.charAt(0) != '#') searchBookByName(text);
            else searchBookById(text.substring(1));
        }
    }

    private String sortString(String str) {
        str = str.toLowerCase();
        char[] charArray = str.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    private int getDifference(String a, String b) {
        int end = Math.min(a.length(), b.length());
        int cnt = 0;

        a = sortString(subString(a, 0, end - 1));
        b = sortString(subString(b, 0, end - 1));

        for (int i = 0; i < end; i++) {
            if (a.charAt(i) != b.charAt(i)) {
                cnt++;
            }
        }

        return cnt;
    }

    private String subString(String a, int l, int r) {
        StringBuilder res = new StringBuilder();
        for (int i = l; i <= r; i++) {
            res.append(a.charAt(i));
        }
        return res.toString();
    }

    private void searchBookByName(String text) {
        ObservableList<BookInfo> filteredList = FXCollections.observableArrayList();

        for (BookInfo bookInfo : bookList) {
            int end;

            if (text.length() > bookInfo.getBookName().length()) {
                end = bookInfo.getBookName().length();
                for (int start = 0; start <= text.length() - end; start++) {
                    String new_string = subString(text, start, start + end - 1);
                    if (new_string.equalsIgnoreCase(bookInfo.getBookName())) {
                        filteredList.add(bookInfo);
                        break;
                    }
                    else if (getDifference(text, bookInfo.getBookName()) <= 2) {
                        filteredList.add(bookInfo);
                        break;
                    }
                }
            }
            else {
                end = text.length();
                for (int start = 0; start <= bookInfo.getBookName().length() - end; start++) {
                    String new_string = subString(bookInfo.getBookName(), start, start + end - 1);
                    if (new_string.equalsIgnoreCase(text)) {
                        filteredList.add(bookInfo);
                        break;
                    }
                    else if (getDifference(text, bookInfo.getBookName()) == 0) {
                        filteredList.add(bookInfo);
                        break;
                    }
                }
            }
        }

        store_tb.setItems(filteredList);
    }


    private void searchBookById(String text) {
        if(text.contains(" ")){
            return;
        }

        int bookId = Integer.parseInt(text);
        ObservableList<BookInfo> filteredList = bookList.stream()
                .filter(book -> book.getBookId() == bookId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
        store_tb.getItems().addListener((ListChangeListener<BookInfo>) change -> {
            if (!change.next() || change.wasAdded() || change.wasRemoved()) {
                filteredList.clear();
            }
        });
    }


    protected void addTableClickListener() {
        store_tb.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                BookDisplay(newValue);
            }
        });
    }

    protected void BookDisplay(BookInfo bookInfo) {
        BookViewingModel.getInstance().setBookInfo(bookInfo);
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Book Viewing");
    }
}
