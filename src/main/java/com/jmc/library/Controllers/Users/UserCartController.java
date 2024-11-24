package com.jmc.library.Controllers.Users;

import com.jmc.library.Controllers.Interface.CartUpdateListener;
import com.jmc.library.Controllers.Interface.InterfaceManager;
import com.jmc.library.Controllers.Notification.NotificationOverlay;
import com.jmc.library.Models.LibraryModel;
import com.jmc.library.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.nio.channels.OverlappingFileLockException;
import java.util.ResourceBundle;

public class UserCartController extends User implements Initializable, CartUpdateListener {
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
        InterfaceManager.getInstance().setCartUpdateListener(this);
        setButtonListener();
        setMaterialListener();
        setListBookVBox();
    }

    @Override
    public void onCartUpdated() {
        updateCartSummary();
    }

    @Override
    public void onRemoveCartEntity(CartEntityController cartEntityController) {
        LibraryModel.getInstance().getUser().removeCartEntityController(cartEntityController);
        setListBookVBox();
        updateCartSummary();
    }

    @Override
    public void onAddCartEntity(CartEntityController cartEntityController) {
        setListBookVBox();
        updateCartSummary();
    }

    private void setButtonListener() {
        back_library_btn.setOnAction(actionEvent -> {
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Store");
        });

        check_out_btn.setOnAction(actionEvent -> {
            LibraryModel.getInstance().getUser().userPayment();
            LibraryModel.getInstance().getUser().clearCart();
            setListBookVBox();
            updateCartSummary();
            try {
                NotificationOverlay overlay = new NotificationOverlay("Payment Successful!!", check_out_btn.getScene());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Model.getInstance().getViewFactory().getSelectedUserMode().set("User Pending");
        });
    }

    private void setListBookVBox() {
        list_book_vbox.getChildren().clear();

        for (CartEntityController cartEntityController : LibraryModel.getInstance().getUser().getCartEntityControllers()) {
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

    private void setMaterialListener() {
        sort_choice_box.getItems().addAll("Sort by name", "Sort by author", "Sort by price");
        sort_choice_box.getSelectionModel().selectFirst();

        sort_choice_box.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, oldValue, newValue) -> {
                    switch (newValue) {
                        case "Sort by name":
                            LibraryModel.getInstance().getUser().getCartEntityControllers().sort((o1, o2) -> o1.getUserBookInfo().getBookName().compareTo(o2.getUserBookInfo().getBookName()));
                            break;
                        case "Sort by author":
                            LibraryModel.getInstance().getUser().getCartEntityControllers().sort((o1, o2) -> o1.getUserBookInfo().getAuthorName().compareTo(o2.getUserBookInfo().getAuthorName()));
                            break;
                        case "Sort by price":
                            LibraryModel.getInstance().getUser().getCartEntityControllers().sort((o1, o2) -> Double.compare(o1.getUserBookInfo().getTotalCost(), o2.getUserBookInfo().getTotalCost()));
                            break;
                    }
                    setListBookVBox();
                });
        updateCartSummary();
    }

    public void updateCartSummary() {
        sub_total_lbl.setText(LibraryModel.getInstance().getUser().getSubTotal() + " $");
        additional_lbl.setText(LibraryModel.getInstance().getUser().getAdditional() + " $");
        total_lbl.setText(LibraryModel.getInstance().getUser().getTotal() + " $");
        show_total_lbl.setText(LibraryModel.getInstance().getUser().getTotal() + " $");
    }
}
