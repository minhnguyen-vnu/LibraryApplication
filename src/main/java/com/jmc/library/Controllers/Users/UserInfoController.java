package com.jmc.library.Controllers.Users;

import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.LibraryModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;

public class UserInfoController {

    public ImageView avatar_img;
    public Button changePhoto_btn;
    public TextField name_txt;
    public TextField dob_txt;
    public TextField mssv_txt;
    public PasswordField password_txt;
    public PasswordField newPassword_txt;
    public PasswordField confirmPassword_txt;
    public Button save_btn;
    public Button cancel_btn;
    public Label status_label;

    @FXML
    public void initialize() {
        setMaterialListener();

        changePhoto_btn.setOnAction(actionEvent -> handleChangePhoto());
        save_btn.setOnAction(actionEvent -> handleSaveChanges());
        cancel_btn.setOnAction(actionEvent -> handleCancel());
    }

    private void setMaterialListener() {
        name_txt.setText(LibraryModel.getInstance().getUser().getName());
        dob_txt.setText(LibraryModel.getInstance().getUser().getBirthDate().toString());
        mssv_txt.setText(String.valueOf(LibraryModel.getInstance().getUser().getID()));
        avatar_img.setImage(LibraryModel.getInstance().getUser().getAvatar());
        password_txt.setText(LibraryModel.getInstance().getUser().getPassword());
    }

    private void handleChangePhoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            LibraryModel.getInstance().getUser()
                    .setAvatar(image);
            avatar_img.setImage(image);
        }
    }

    private void handleSaveChanges() {
        String name = name_txt.getText();
        String dob = dob_txt.getText();
        String mssv = mssv_txt.getText();
        String password = password_txt.getText();
        String newPassword = newPassword_txt.getText();
        String confirmPassword = confirmPassword_txt.getText();
        ImageView avatar = avatar_img;

        if (!newPassword.equals(confirmPassword)) {
            setStatusMessage("Wrong new password", "red");
            return;
        }

        if (password.isEmpty() || !password.equals(LibraryModel.getInstance().getUser().getPassword())) {
            setStatusMessage("Wrong recent password", "red");
            return;
        }

        LibraryModel.getInstance().getUser().setName(name);
        LibraryModel.getInstance().getUser().setBirthDate(LocalDate.parse(dob));
        LibraryModel.getInstance().getUser().setID(Integer.parseInt(mssv));

        if (!newPassword.isEmpty()) {
            LibraryModel.getInstance().getUser().setPassword(newPassword);
        }

        DBUpdate dbUpdate = new DBUpdate("Update users set name = ?, birthDate = ?, password = ?, ID = ?, imageView = ? where username = ?",
                name,
                LocalDate.parse(dob),
                newPassword,
                mssv,
                LibraryModel.getInstance().getUser().getUsername(),
                ImageUtils.imageToByteArray(avatar.getImage()));
        dbUpdate.setOnSucceeded(event -> {
            setStatusMessage("Saved completed", "green");
        });
        Thread thread = new Thread(dbUpdate);
        thread.start();
    }

    private void handleCancel() {
        setMaterialListener();
        setStatusMessage("", "transparent");
    }

    private void setStatusMessage(String message, String color) {
        status_label.setText(message);
        status_label.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold; -fx-font-size: 14px;");
    }
}