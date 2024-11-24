package com.jmc.library.View;

import com.jmc.library.Assets.BookInfo;
import com.jmc.library.Controllers.Authentication;
import com.jmc.library.Controllers.Books.BookDisplayController;
import com.jmc.library.Controllers.Users.UserView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.ParallelCamera;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {
    private HBox logInView;
    private HBox signUpView;
    private HBox userLibrary;
    private HBox userHiredBook;
    private HBox userDashboard;
    private Node userCart;
    private Node userPending;
    private Node bookDetail;
    private FXMLLoader notificationView;
    private final StringProperty selectedAuthenticatonMode;
    private final StringProperty selectedUserMode;

    private BookDisplayController bookDisplayController;

    public ViewFactory() {
        this.selectedAuthenticatonMode = new SimpleStringProperty("");
        this.selectedUserMode = new SimpleStringProperty("");
    }

    public StringProperty getSelectedAuthenticatonMode() {
        return selectedAuthenticatonMode;
    }

    public StringProperty getSelectedUserMode() {
        return selectedUserMode;
    }

    public HBox getSignUpView() {
        if (signUpView == null) {
            try {
                signUpView = new FXMLLoader(getClass().getResource("/FXML/SignUp.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return signUpView;
    }

    public HBox getLogInView() {
        if (logInView == null) {
            try {
                logInView = new FXMLLoader(getClass().getResource("/FXML/Login.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return logInView;
    }

    public void showAuthenticationWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/Authentication.fxml"));
        Authentication authentication = new Authentication();
        fxmlLoader.setController(authentication);
        createStage(fxmlLoader);
    }

    public HBox getUserLibrary() {
        if (userLibrary == null) {
            try {
                userLibrary = new FXMLLoader(getClass().getResource("/FXML/Library.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userLibrary;
    }

    public HBox getUserStore() {
        return getUserLibrary();
    }
    public HBox getUserHiredBook() {
        if (userHiredBook == null) {
            try {
                userHiredBook = new FXMLLoader(getClass().getResource("/FXML/HiredBook.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userHiredBook;
    }

    public HBox getUserDashboard() {
        if (userDashboard == null) {
            try {
                System.out.println("ViewFactory.getUserDashboard");
                userDashboard = new FXMLLoader(getClass().getResource("/FXML/UserDashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userDashboard;
    }

    public Node getUserCart() {
        if (userCart == null) {
            try {
                userCart = new FXMLLoader(getClass().getResource("/FXML/Cart.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userCart;
    }

    public Node getBookDetail() {
        if (bookDetail == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/DisplayBook.fxml"));
                bookDetail = loader.load();
                bookDisplayController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bookDetail;
    }

    public FXMLLoader getNotificationView() {
        if (notificationView == null) {
            try {
                notificationView = new FXMLLoader(getClass().getResource("/FXML/Notification.fxml"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return notificationView;
    }

    public void setBookDisplayController(BookDisplayController bookDisplayController) {
        this.bookDisplayController = bookDisplayController;
    }

    public BookDisplayController getBookDisplayController() {
        return bookDisplayController;
    }

    public Node getUserPending() {
        if (userPending == null) {
            try {
                userPending = new FXMLLoader(getClass().getResource("/FXML/UserPending.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userPending;
    }

    public void showUserWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/UserView.fxml"));
        UserView user = new UserView();
        fxmlLoader.setController(user);
        createStage(fxmlLoader);
    }

    public void showAdminWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/AdminLibrary.fxml"));
        createStage(fxmlLoader);
    }

    public void showUserDashboard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/UserDashboard.fxml"));
        createStage(fxmlLoader);
    }

    public void showAdminLibrary() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/AdminLibrary.fxml"));
        createStage(fxmlLoader);
    }

    public void showDisplayBook() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/DisplayBook.fxml"));
        createStage(fxmlLoader);
    }
    void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            System.out.println("ViewFactory.createStage");
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Library Application");
        stage.show();
    }

    void createStage(Parent loader) {
        Scene scene = new Scene(loader);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Library Application");
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }

    public void resetAll() {
        System.out.println("ViewFactory.resetAll");
        logInView = null;
        signUpView = null;
        userLibrary = null;
        userHiredBook = null;
        userDashboard = null;
        userCart = null;
        userPending = null;
        bookDetail = null;
        notificationView = null;
    }

    public ThreadLocal<String> getSelectedAdminMode() {
        return null;
    }
}
