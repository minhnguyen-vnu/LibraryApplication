package com.jmc.library.Controllers.Users;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Abstract class for managing the user library table, including displaying and searching books.
 */
public abstract class UserLibraryTable {
    public TableColumn<UserBookInfo, String> book_name_tb_cl;
    public TableColumn<UserBookInfo, String> author_tb_cl;
    public TableColumn<UserBookInfo, Integer> book_id_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> return_day_tb_cl;
    public TableColumn<UserBookInfo, LocalDate> picked_day_tb_cl;
    public TableView<UserBookInfo> store_tb;
    protected ObservableList<UserBookInfo> bookList;
    public TableColumn<UserBookInfo, ImageView> book_cover_tb_cl;

    public Button go_to_store_btn;
    public Button go_to_library_btn;
    public Button back_to_dashboard_btn;

    public ImageView account_avatar_img;

    public TextField search_fld;
    public Button search_btn;
    public Button log_out_btn;
    public Button cart_btn;
    public Button pending_btn;
    public Button go_to_setting_btn;
    public Button reload_btn;

    public ChoiceBox<String> num_row_shown;
    public Label username_lbl;

    /**
     * Adds bindings to the UI components.
     */
    protected void addBinding() {
        book_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookName"));
        author_tb_cl.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        book_id_tb_cl.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        picked_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("pickedDate"));
        return_day_tb_cl.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        book_cover_tb_cl.setCellValueFactory(new PropertyValueFactory<>("imageView"));
        account_avatar_img.imageProperty().bind(LibraryModel.getInstance().getUser().avatarProperty());
        Circle clip = new Circle(account_avatar_img.getFitWidth() / 2, account_avatar_img.getFitHeight() / 2, Math.min(account_avatar_img.getFitWidth(), account_avatar_img.getFitHeight()) / 2);
        account_avatar_img.setClip(clip);
        store_tb.setItems(bookList);

        LibraryModel.getInstance().getUser().avatarProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("-100");
        });
    }

    /**
     * Adds a loading placeholder to the table.
     */
    protected void addLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Loading.fxml"));
        try {
            ImageView loading_img = loader.load();
            store_tb.setPlaceholder(loading_img);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a placeholder for when no data is available.
     */
    protected void returnLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/NoDataPlaceHolder.fxml"));
        try {
            Label label = loader.load();
            store_tb.setPlaceholder(label);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Abstract method to set the table data.
     */
    protected abstract void setTable();

    /**
     * Abstract method to show the library data.
     */
    protected abstract void showLibrary();

    /**
     * Searches for books by name.
     *
     * @param text The text to search for.
     */
    private void searchBook(String text) {
        if (!text.isEmpty()) {
            searchBookByName(text);
        }
        else {
            store_tb.setItems(bookList);
        }

    }

    /**
     * Sorts a string alphabetically.
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
     * Gets the difference between two strings.
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
     * Gets a substring of a string.
     *
     * @param a The string.
     * @param l The start index.
     * @param r The end index.
     * @return The substring.
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
     * @param text The text to search for.
     */
    private void searchBookByName(String text) {
        ObservableList<UserBookInfo> filteredList = FXCollections.observableArrayList();

        for (UserBookInfo bookInfo : bookList) {
            int end;

            if (text.length() > bookInfo.getBookName().length()) {
                end = bookInfo.getBookName().length();
                for (int start = 0; start <= text.length() - end; start++) {
                    String new_string = subString(text, start, start + end - 1);
                    if (new_string.equalsIgnoreCase(bookInfo.getBookName())) {
                        filteredList.add(bookInfo);
                        break;
                    } else if (getDifference(text, bookInfo.getBookName()) <= 2) {
                        filteredList.add(bookInfo);
                        break;
                    }
                }
            } else {
                end = text.length();
                for (int start = 0; start <= bookInfo.getBookName().length() - end; start++) {
                    String new_string = subString(bookInfo.getBookName(), start, start + end - 1);
                    if (new_string.equalsIgnoreCase(text)) {
                        filteredList.add(bookInfo);
                        break;
                    } else if (getDifference(text, bookInfo.getBookName()) <= 2) {
                        filteredList.add(bookInfo);
                        break;
                    }
                }
            }
        }

        store_tb.setItems(filteredList);
    }

    /**
     * Sets the username label.
     */
    protected void setUsername_lbl() {
        username_lbl.setText(LibraryModel.getInstance().getUser().getName());
    }

    /**
     * Sets the number of rows shown in the table.
     */
    protected void setNum_row_shown() {
        num_row_shown.getItems().addAll("5", "10", "15", "20", "All");
        num_row_shown.setValue("All");
        num_row_shown.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            store_tb.refresh();
            if (newVal.equals("All")) {
                store_tb.setItems(FXCollections.observableArrayList(bookList));
            } else {
                store_tb.setItems(FXCollections.observableArrayList(bookList.stream().limit(Integer.parseInt(newVal)).collect(Collectors.toList())));
            }
        });
    }

    /**
     * Sets the listener for material changes.
     */
    protected void setListenerMaterial() {
        setUsername_lbl();
        setNum_row_shown();
        LibraryModel.getInstance().getUser().nameProperty().addListener(((observableValue, s, t1) -> {
            username_lbl.setText(t1);
        }));
    }

    /**
     * Sets the button listeners for various actions.
     */
    protected void setButtonListener() {
        search_btn.setOnAction(actionEvent -> searchBook(search_fld.getText()));
        search_fld.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                searchBook(search_fld.getText());
            }
        });
        go_to_store_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });
        go_to_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });
        log_out_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().resetAll();
            DBUpdate dbUpdate = new DBUpdate("update users\n" +
                    "set status = ?\n" +
                    "where username = ?;", "offline", LibraryModel.getInstance().getUser().getUsername());
            Thread thread = new Thread(dbUpdate);
            thread.setDaemon(true);
            thread.start();
            LibraryModel.getInstance().getUser().resetAll();
            stageTransforming();
        });

        back_to_dashboard_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Dashboard");
        });
        cart_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Cart");
        });
        pending_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });
        go_to_setting_btn.setOnAction(actionEvent -> {
            Scene currentScene = go_to_setting_btn.getScene();
            try {
                UserInfoOverlay userInfoOverlay = new UserInfoOverlay(currentScene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        reload_btn.setOnAction(actionEvent -> showLibrary());
    }

    /**
     * Transforms the stage to the authentication window.
     */
    private void stageTransforming() {
        Stage currentStage = (Stage) log_out_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(currentStage);
        Model.getInstance().getViewFactory().showAuthenticationWindow();
        Model.getInstance().getViewFactory().getSelectedAuthenticatonMode().set("Login");
        Model.getInstance().getViewFactory().getSelectedAdminMode().set("");
    }
}
