package com.jmc.library.Models;

import com.jmc.library.Assets.UserCartInfo;
import com.jmc.library.Controllers.Users.CartEntityController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 * Model class for the cart view.
 */
public class CartModel {
    private static CartModel cartModel;
    private UserCartInfo userCartInfo;

    private CartModel() {
        userCartInfo = new UserCartInfo();
    }

    public static CartModel getInstance() {
        if (cartModel == null) cartModel = new CartModel();
        return cartModel;
    }

    public UserCartInfo getUserCartInfo() {
        return userCartInfo;
    }

    public void setUserCartInfo(UserCartInfo userCartInfo) {
        this.userCartInfo = userCartInfo;
    }
}
