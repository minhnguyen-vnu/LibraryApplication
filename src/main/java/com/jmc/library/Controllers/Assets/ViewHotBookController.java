package com.jmc.library.Controllers.Assets;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewHotBookController implements Initializable {

    public FlowPane book_holder_flow;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addBookList();
    }

    public void addBookList() {
        for(int i = 0; i < 15; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Book.fxml"));
                book_holder_flow.getChildren().add(fxmlLoader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
