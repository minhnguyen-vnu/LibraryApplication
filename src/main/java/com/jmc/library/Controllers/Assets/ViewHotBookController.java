package com.jmc.library.Controllers.Assets;

import com.jmc.library.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewHotBookController implements Initializable {

    @FXML
    public FlowPane book_holder_flow;
    @FXML
    public Label back_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addListener();
        addBookList();
    }


    public void addListener() {
        back_btn.setOnMouseClicked(e -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().setValue("User Dashboard");
        });
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
