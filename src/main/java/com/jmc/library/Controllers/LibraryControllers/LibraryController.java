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

/**
 * Controller class for managing the library view, including searching and displaying books.
 */
public class LibraryController extends LibraryTable {
    public TextField search_fld;
    public Button search_btn;
    public TableColumn<BookInfo, ImageView> book_cover_tb_cl;

    /**
     * Initializes the controller and sets up the initial actions.
     */
    protected void initialAction(){
        search_btn.setOnAction(actionEvent -> searchBook(search_fld.getText()));
        search_fld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBook(search_fld.getText());
            }
        });
    }

    /**
     * Searches for books based on the provided text.
     *
     * @param text The text to search for.
     */
    private void searchBook(String text) {
        if (text.isEmpty()) {
            store_tb.setItems(bookList);
        } else {
            if (text.charAt(0) == '@') searchBookByAuthor(text.substring(1));
            else if (text.charAt(0) != '#') searchBookByName(text);
            else searchBookById(text.substring(1));
        }
    }

    /**
     * Sorts the given string in alphabetical order.
     *
     * @param str The string to sort.
     * @return The sorted string.
     */
    private String sortString(String str) {
        str = str.toLowerCase();
        char[] charArray = str.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    /**
     * Calculates the difference between two strings.
     *
     * @param a The first string.
     * @param b The second string.
     * @return The number of differing characters.
     */
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

    /**
     * Extracts a substring from the given string.
     *
     * @param a The string to extract from.
     * @param l The starting index.
     * @param r The ending index.
     * @return The extracted substring.
     */
    private String subString(String a, int l, int r) {
        StringBuilder res = new StringBuilder();
        for (int i = l; i <= r; i++) {
            res.append(a.charAt(i));
        }
        return res.toString();
    }

    /**
     * Searches for books by name.
     *
     * @param text The name to search for.
     */
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


    /**
     * Searches for books by ID.
     *
     * @param text The ID to search for.
     */
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

    private void searchBookByAuthor(String text) {
        ObservableList<BookInfo> filteredList = FXCollections.observableArrayList();

        for (BookInfo bookInfo : bookList) {
            int end;

            if (text.length() > bookInfo.getAuthorName().length()) {
                end = bookInfo.getAuthorName().length();
                for (int start = 0; start <= text.length() - end; start++) {
                    String new_string = subString(text, start, start + end - 1);
                    if (new_string.equalsIgnoreCase(bookInfo.getAuthorName())) {
                        filteredList.add(bookInfo);
                        break;
                    }
                    else if (getDifference(text, bookInfo.getAuthorName()) <= 2) {
                        filteredList.add(bookInfo);
                        break;
                    }
                }
            }
            else {
                end = text.length();
                for (int start = 0; start <= bookInfo.getAuthorName().length() - end; start++) {
                    String new_string = subString(bookInfo.getAuthorName(), start, start + end - 1);
                    if (new_string.equalsIgnoreCase(text)) {
                        filteredList.add(bookInfo);
                        break;
                    }
                    else if (getDifference(text, bookInfo.getAuthorName()) == 0) {
                        filteredList.add(bookInfo);
                        break;
                    }
                }
            }
        }

        store_tb.setItems(filteredList);

    }
}
