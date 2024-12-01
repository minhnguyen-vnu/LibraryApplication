package com.jmc.library.Controllers.Assets;

import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class CommentEntityController implements Initializable {

    @FXML
    public ImageView commenter_avatar;
    @FXML
    public Label commenter_name;
    @FXML
    public TextArea comment_area;
    @FXML
    public Button like_btn;
    @FXML
    public Button edit_btn;
    @FXML
    public Button hide_btn;
    @FXML
    public VBox container;

    protected int commenter_id;
//    private int like_count;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMaterial();
        setListener();
    }

    public void setMaterial() {
        Circle clip = new Circle(25, 25, 25);
        commenter_avatar.setClip(clip);
        commenter_avatar.setImage(LibraryModel.getInstance().getUser().getAvatar());
        commenter_name.setText(LibraryModel.getInstance().getUser().getName());
        comment_area.setText("This is a comment.");
        commenter_id = LibraryModel.getInstance().getUser().getID();
    }

    public void setListener() {

        comment_area.setEditable(false);
        comment_area.setWrapText(true);
        if (commenter_id != LibraryModel.getInstance().getUser().getID()) {
            edit_btn.setVisible(false);
        }
        else {
            edit_btn.setOnAction(event -> {
                comment_area.setEditable(true);
                comment_area.requestFocus();
            });
        }
        comment_area.setOnKeyPressed(event -> {
            if (event.getCode().getName().equals("Enter")) {
                comment_area.setEditable(false);
            }
        });


        hide_btn.setOnAction(event -> {
            container.setVisible(false);
            container.setPrefHeight(0);
        });
    }
}
