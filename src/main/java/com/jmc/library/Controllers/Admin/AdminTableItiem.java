package com.jmc.library.Controllers.Admin;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Class for managing the table items in the admin panel.
 */
public class AdminTableItiem {
    private CheckBox checkOut;
    private String avatarImagePath;
    private String name;
    private String id;
    private String pickedDay;
    private String returnDay;
    private String totalCost;
    private String actionImagePath;

    private HBox nameHBox;
    private Button action;

    public AdminTableItiem(CheckBox checkOut, String actionImagePath, String name, String id, String pickedDay, String returnDay, String totalCost, String avatarImagePath) {
        this.checkOut = checkOut;
        this.avatarImagePath = getClass().getResource(avatarImagePath).toExternalForm();
        this.name = name;
        this.nameHBox = createHBoxWithImageAndText(this.avatarImagePath, name);
        this.id = id;
        this.pickedDay = pickedDay;
        this.returnDay = returnDay;
        this.totalCost = totalCost;
        this.actionImagePath = getClass().getResource(actionImagePath).toExternalForm();
        this.action = createButtonWithImage(this.actionImagePath);
    }

    /**
     * Creates a button with an image.
     *
     * @param imagePath the path to the image.
     * @return the button with the image.
     */
    private Button createButtonWithImage(String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(16);
        imageView.setFitHeight(10);
        Button button = new Button();
        button.setGraphic(imageView);
        return button;
    }

    /**
     * Creates a HBox with an image and text.
     *
     * @param imagePath the path to the image.
     * @param text      the text to display.
     * @return the HBox with the image and text.
     */
    private HBox createHBoxWithImageAndText(String imagePath, String text) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        HBox hBox = new HBox();
        hBox.getChildren().add(imageView);
        hBox.getChildren().add(new Label(text));
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    public CheckBox getCheckOut() {
        return checkOut;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getPickedDay() {
        return pickedDay;
    }

    public String getReturnDay() {
        return returnDay;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public Button getAction() {
        return action;
    }

    public void setAction(Button action) {
        this.action = action;
    }

    public HBox getNameHBox() {
        return nameHBox;
    }
}