package com.jmc.library.Controllers.Notification;

import com.jmc.library.Assets.UserBookInfo;
import com.jmc.library.Controllers.Users.CartEntityController;
import com.jmc.library.Models.CartModel;
import com.jmc.library.Models.LibraryModel;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.LocalDate;

public interface BookAddingNotification {

    /**
     * Adds a book to the user's cart.
     *
     * @param addedBook The book to add.
     * @throws IOException If the FXML file for the notification overlay cannot be found.
     */
    static void addBookForUser(UserBookInfo addedBook, Scene currentScene) throws IOException {
        if(CartModel.getInstance().getUserCartInfo().getCartList().stream()
                .anyMatch(cartEntityController -> cartEntityController
                        .getUserBookInfo().getBookId() == addedBook.getBookId())) {
            NotificationOverlay.notificationScreen("Book already in cart.", currentScene);
            return;
        }

        if(LibraryModel.getInstance().getUser().getPendingBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())) {
            NotificationOverlay.notificationScreen("Book already requested.", currentScene);
            return;
        }

        if(LibraryModel.getInstance().getUser().getBorrowedBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getBookId() == addedBook.getBookId())
                && LibraryModel.getInstance().getUser().getBorrowedBookList().stream()
                .anyMatch(userBookInfo -> userBookInfo.getReturnDate().isAfter(LocalDate.now()))) {
            NotificationOverlay.notificationScreen("Book already borrowed.", currentScene);
            return;
        }

        CartModel.getInstance().getUserCartInfo().getCartList().add(new CartEntityController(addedBook));
        CartModel.getInstance().getUserCartInfo().AddCartEntity(new CartEntityController(addedBook));

        NotificationOverlay.notificationScreen("Book added to cart.", currentScene);
    }
}
