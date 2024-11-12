package com.jmc.library.Controllers.Interface;

import com.jmc.library.Controllers.Users.CartEntityController;

public interface CartUpdateListener {
    void onCartUpdated();

    void onRemoveCartEntity(CartEntityController cartEntityController);

    void onAddCartEntity(CartEntityController cartEntityController);
}
