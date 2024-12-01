package com.jmc.library.Controllers.Assets;

import com.jmc.library.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class TopRatedBooksController implements Initializable {
    public FlowPane book_holder_flow;
    public Label back_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addAction();
        addBookList();
    }

    private void addAction() {
        back_btn.setOnMouseClicked(mouseEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Library");
        });
    }

    private void addBookList() {
        for(int i = 0; i < 15; i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/TopRatedBook.fxml"));
                book_holder_flow.getChildren().add(fxmlLoader.load());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
