package com.jmc.library.Controllers.Users;

import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.Database.DBUpdate;
import com.jmc.library.Models.LibraryModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;

public class UserInfoController implements Initializable {

    public ImageView avatar_img;
    public Button changePhoto_btn;
    public TextField name_txt;
    public DatePicker dob_txt;
    public TextField mssv_txt;
    public PasswordField password_txt;
    public PasswordField newPassword_txt;
    public PasswordField confirmPassword_txt;
    public Button save_btn;
    public Button cancel_btn;
    public Label status_label;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setMaterialListener();
        changePhoto_btn.setOnAction(actionEvent -> handleChangePhoto());
        save_btn.setOnAction(actionEvent -> handleSaveChanges());
        cancel_btn.setOnAction(actionEvent -> handleCancel());
    }

    private void setMaterialListener() {
        name_txt.setText(LibraryModel.getInstance().getUser().getName());
        dob_txt.setValue(LibraryModel.getInstance().getUser().getBirthDate());
        mssv_txt.setText(String.valueOf(LibraryModel.getInstance().getUser().getID()));
        avatar_img.setImage(LibraryModel.getInstance().getUser().getAvatar());
        Circle clip = new Circle(avatar_img.getFitWidth() / 2, avatar_img.getFitHeight() / 2, Math.min(avatar_img.getFitWidth(), avatar_img.getFitHeight()) / 2);
        avatar_img.setClip(clip);
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
//            LibraryModel.getInstance().getUser()
//                    .setAvatar(image);
            avatar_img.setImage(image);
        }
    }

    private void handleSaveChanges() {
        String name = name_txt.getText();
        LocalDate dob = dob_txt.getValue();
        String mssv = mssv_txt.getText();
        String password = password_txt.getText();
        String newPassword = newPassword_txt.getText();
        String confirmPassword = confirmPassword_txt.getText();
        ImageView avatar = avatar_img;

        if(password == null) {
            password = "";
        }
        if(newPassword == null) {
            newPassword = "";
        }
        if(confirmPassword == null) {
            confirmPassword = "";
        }

        if(!newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (!newPassword.equals(confirmPassword)) {
                setStatusMessage("Wrong new password", "red");
                return;
            }

            if (password.isEmpty() || !password.equals(LibraryModel.getInstance().getUser().getPassword())) {
                setStatusMessage("Wrong current password", "red");
                return;
            }
        }

        LibraryModel.getInstance().getUser().setName(name);
        LibraryModel.getInstance().getUser().setBirthDate(dob);
        LibraryModel.getInstance().getUser().setID(Integer.parseInt(mssv));

        if (!newPassword.isEmpty()) {
            LibraryModel.getInstance().getUser().setPassword(newPassword);
            password = newPassword;
        }
        else {
            password = LibraryModel.getInstance().getUser().getPassword();
        }


        byte[] imageBytes = ImageUtils.imageToByteArray(avatar_img.getImage());
        if (imageBytes.length > 0) {
            DBUpdate dbUpdate = new DBUpdate("Update users set name = ?, birthDate = ?, password = ?, ID = ?, imageView = ? where username = ?",
                    name, dob,  password, mssv,
                    ImageUtils.imageToByteArray(avatar.getImage()),
                    LibraryModel.getInstance().getUser().getUsername()
            );
            dbUpdate.setOnSucceeded(event -> {
                LibraryModel.getInstance().getUser().loadUserInfo();
                setStatusMessage("Saved completed", "green");
            });
            Thread thread = new Thread(dbUpdate);
            thread.setDaemon(true);
            thread.start();
        }
        else {
            DBUpdate dbUpdate = new DBUpdate("Update users set name = ?, birthDate = ?, password = ?, ID = ? where username = ?",
                    name, dob,  password, mssv,
                    LibraryModel.getInstance().getUser().getUsername()
            );
            dbUpdate.setOnSucceeded(event -> {
                LibraryModel.getInstance().getUser().loadUserInfo();
                setStatusMessage("Saved completed", "green");
            });
            Thread thread = new Thread(dbUpdate);
            thread.setDaemon(true);
            thread.start();
        }
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
