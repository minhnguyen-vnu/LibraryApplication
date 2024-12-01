package com.jmc.library.Controllers.Assets;

import com.jmc.library.Models.Model;
import com.jmc.library.Models.TopBookModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

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
        for(int i = 0; i < TopBookModel.getInstance().getTopBookList().size(); i++) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/TopRatedBook.fxml"));
                VBox bookPane = fxmlLoader.load();
                TopBookController controller = fxmlLoader.getController();
                controller.setBookInfo(TopBookModel.getInstance().getTopBookList().get(i));
                book_holder_flow.getChildren().add(bookPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}