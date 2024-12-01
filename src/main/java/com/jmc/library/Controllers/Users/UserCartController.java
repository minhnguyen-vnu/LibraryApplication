package com.jmc.library.Controllers.Users;

import com.jmc.library.Controllers.Notification.NotificationOverlay;
import com.jmc.library.Models.CartModel;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for managing the user cart, including displaying books and handling checkout.
 */
public class UserCartController implements Initializable {
    public Button back_library_btn;
    public ChoiceBox<String> sort_choice_box;
    public VBox list_book_vbox;
    public Label sub_total_lbl;
    public Label additional_lbl;
    public Label total_lbl;
    public Button check_out_btn;
    public Label show_total_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonListener();
        setMaterialListener();
        addBinding();
    }

    /**
     * Adds bindings to the UI components.
     */
    private void addBinding() {
        setListBookVBox();
        sub_total_lbl.setText(CartModel.getInstance().getUserCartInfo().getTotal());
        additional_lbl.setText(CartModel.getInstance().getUserCartInfo().getAdditional());
        total_lbl.setText(CartModel.getInstance().getUserCartInfo().getTotal());
        show_total_lbl.setText(CartModel.getInstance().getUserCartInfo().getTotal());

        CartModel.getInstance().getUserCartInfo().getList_book_vbox().getChildren().addListener((ListChangeListener<Node>) change -> {
            while (change.next()) {
                setListBookVBox();
                break;
            }
        });

        CartModel.getInstance().getUserCartInfo().getSub_total_lbl().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                sub_total_lbl.setText(newValue);
                total_lbl.setText(newValue);
                show_total_lbl.setText(newValue);
            }
        });
    }

    /**
     * Sets the button listeners for navigation and actions.
     */
    private void setButtonListener() {
        back_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        check_out_btn.setOnAction(actionEvent -> {
            LibraryModel.getInstance().getUser().userPayment();
            CartModel.getInstance().getUserCartInfo().getCartList().clear();
            setListBookVBox();
            try {
                NotificationOverlay.notificationScreen("Payment Successful!!", check_out_btn.getScene());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });
    }

    /**
     * Sets the list of books in the VBox.
     */
    private void setListBookVBox() {
        list_book_vbox.getChildren().clear();

        for (CartEntityController cartEntityController : CartModel.getInstance().getUserCartInfo().getCartList()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CartEntity.fxml"));
            loader.setController(cartEntityController);
            try {
                Node newNode = loader.load();
                list_book_vbox.getChildren().add(newNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        list_book_vbox.layout();
    }

    /**
     * Sets the initial values for the cart fields and choice box.
     */
    private void setMaterialListener() {
        sort_choice_box.getItems().addAll("Sort by name", "Sort by author", "Sort by price");
        sort_choice_box.getSelectionModel().selectFirst();

        sort_choice_box.getSelectionModel().selectedItemProperty()
            .addListener((observableValue, oldValue, newValue) -> {
                switch (newValue) {
                    case "Sort by name":
                        CartModel.getInstance().getUserCartInfo().getCartList().sort((o1, o2) -> o1.getUserBookInfo().getBookName().compareTo(o2.getUserBookInfo().getBookName()));
                        break;
                    case "Sort by author":
                        CartModel.getInstance().getUserCartInfo().getCartList().sort((o1, o2) -> o1.getUserBookInfo().getAuthorName().compareTo(o2.getUserBookInfo().getAuthorName()));
                        break;
                    case "Sort by price":
                        CartModel.getInstance().getUserCartInfo().getCartList().sort((o1, o2) -> Double.compare(o1.getUserBookInfo().getTotalCost(), o2.getUserBookInfo().getTotalCost()));
                        break;
                }
                setListBookVBox();
            });
    }
}
