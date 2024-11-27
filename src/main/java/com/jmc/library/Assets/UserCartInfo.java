package com.jmc.library.Assets;

import com.jmc.library.Controllers.Users.CartEntityController;
import com.jmc.library.Models.CartModel;
import com.jmc.library.Models.LibraryModel;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;

public class UserCartInfo {
    private ObservableList<CartEntityController> cartList;
    private Label show_total_lbl;
    private Label total_lbl;
    private ObjectProperty<VBox> list_book_vbox;
    private Label sub_total_lbl;
    private Label additional_lbl;

    public UserCartInfo() {
        cartList = FXCollections.observableArrayList();
        list_book_vbox = new SimpleObjectProperty<>(new VBox());
        sub_total_lbl = new Label();
        additional_lbl = new Label();
        total_lbl = new Label();
        show_total_lbl = new Label();
    }

    public ObservableList<CartEntityController> getCartList() {
        return cartList;
    }

    public void setCartList(ObservableList<CartEntityController> cartList) {
        this.cartList = cartList;
    }

    public Label getShow_total_lbl() {
        return show_total_lbl;
    }

    public void setShow_total_lbl(Label show_total_lbl) {
        this.show_total_lbl = show_total_lbl;
    }

    public Label getTotal_lbl() {
        return total_lbl;
    }

    public void setTotal_lbl(Label total_lbl) {
        this.total_lbl = total_lbl;
    }

    public VBox getList_book_vbox() {
        return list_book_vbox.get();
    }

    public void setList_book_vbox(VBox list_book_vbox) {
        this.list_book_vbox.set(list_book_vbox);
    }

    public ObjectProperty<VBox> listBookVBoxProperty() {
        return list_book_vbox;
    }

    public Label getSub_total_lbl() {
        return sub_total_lbl;
    }

    public void setSub_total_lbl(Label sub_total_lbl) {
        this.sub_total_lbl = sub_total_lbl;
    }

    public Label getAdditional_lbl() {
        return additional_lbl;
    }

    public void setAdditional_lbl(Label additional_lbl) {
        this.additional_lbl = additional_lbl;
    }

    public String getSubTotal() {
        double total = cartList.stream().mapToDouble(cartEntityController -> cartEntityController.getUserBookInfo().getTotalCost()).sum();
        total = Math.round(total * 100.0) / 100.0;
        return Double.toString(total);
    }

    public String getAdditional() {
        return "0.00";
    }

    public String getTotal() {
        return String.format("%.2f",Double.parseDouble(getSubTotal()) + Double.parseDouble(getAdditional()));
    }

    public void setListBookVBox() {
        list_book_vbox.get().getChildren().clear();

        for (CartEntityController cartEntityController : CartModel.getInstance().getUserCartInfo().getCartList()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/CartEntity.fxml"));
            loader.setController(cartEntityController);
            try {
                Node newNode = loader.load();
                list_book_vbox.get().getChildren().add(newNode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        list_book_vbox.get().layout();
    }

    public void updateCartSummary() {
        sub_total_lbl.setText(getSubTotal() + " $");
        additional_lbl.setText(getAdditional() + " $");
        total_lbl.setText(getTotal() + " $");
        show_total_lbl.setText(getTotal() + " $");
    }

    public void CartUpdate() {
        updateCartSummary();
    }

    public void DeleteCartEntity(CartEntityController cartEntityController) {
        cartList.remove(cartEntityController);
        setListBookVBox();
        updateCartSummary();
    }

    public void AddCartEntity(CartEntityController cartEntityController) {
        setListBookVBox();
        updateCartSummary();
    }

    public void clear() {
        cartList.clear();
        setListBookVBox();
        updateCartSummary();
    }
}
