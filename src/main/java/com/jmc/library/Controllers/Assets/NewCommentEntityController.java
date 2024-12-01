package com.jmc.library.Controllers.Assets;

import com.jmc.library.Models.LibraryModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class NewCommentEntityController implements Initializable {

    @FXML
    public ImageView commenter_avatar;
    @FXML
    public TextArea comment_area;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMaterial();
        setListener();
    }

    public void setMaterial() {
        Circle clip = new Circle(25, 25, 25);
        commenter_avatar.setClip(clip);
        commenter_avatar.setImage(LibraryModel.getInstance().getUser().getAvatar());
        comment_area.setPromptText("Enter a new comment.");
        comment_area.setWrapText(true);
    }

    public void setListener() {
        comment_area.setEditable(true);
        comment_area.setOnKeyPressed(e -> {
            if (e.getCode().toString().equals("ENTER")) {
                // Add the comment to the comment_list
            }
        });
    }
}
