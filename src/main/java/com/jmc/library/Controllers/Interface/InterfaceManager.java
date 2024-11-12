package com.jmc.library.Controllers.Interface;

public class InterfaceManager {
    private static InterfaceManager instance;

    private CartUpdateListener cartUpdateListener;

    private InterfaceManager() {
        cartUpdateListener = null;
    }

    public static synchronized InterfaceManager getInstance() {
        if (instance == null) instance = new InterfaceManager();
        return instance;
    }

    public void setCartUpdateListener(CartUpdateListener listener) {
        this.cartUpdateListener = listener;
    }

    public CartUpdateListener getCartUpdateListener() {
        return this.cartUpdateListener;
    }
}
