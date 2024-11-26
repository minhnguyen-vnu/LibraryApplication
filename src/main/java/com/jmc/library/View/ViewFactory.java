package com.jmc.library.View;

import com.jmc.library.Controllers.Admin.AdminView;
import com.jmc.library.Controllers.Authentication;
import com.jmc.library.Controllers.Books.UserBookDisplayController;
import com.jmc.library.Controllers.LibraryControllers.UserLibraryController;
import com.jmc.library.Controllers.Users.UserBookController;
import com.jmc.library.Controllers.Users.UserCartController;
import com.jmc.library.Controllers.Users.UserPendingController;
import com.jmc.library.Controllers.Users.UserView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private HBox adminLibrary;
    private HBox adminSettings;
    private HBox adminDashboard;
    private HBox adminRequestManagement;
    private HBox adminPendingRequestManagement;
    private VBox adminBookViewing;
    private final StringProperty selectedAuthenticatonMode;
    private final StringProperty selectedUserMode;
    private final StringProperty selectedAdminMode;

    private UserBookDisplayController bookDisplayController;
    private UserLibraryController userStore;
    private UserCartController userCartController;
    private UserPendingController userPendingController;
    private UserBookController userBookController;

    public ViewFactory() {
        this.selectedAuthenticatonMode = new SimpleStringProperty("");
        this.selectedUserMode = new SimpleStringProperty("");
        this.selectedAdminMode = new SimpleStringProperty("");
    }

    public StringProperty getSelectedAuthenticatonMode() {
        return selectedAuthenticatonMode;
    }

    public StringProperty getSelectedUserMode() {
        return selectedUserMode;
    }

//    public StringProperty getSelectedAdminMode() { return selectedAdminMode; }

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Library.fxml"));
                userLibrary = loader.load();
                userStore = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userLibrary;
    }

    public UserLibraryController getUserStoreController() {
        return userStore;
    }

    public HBox getUserStore() {
        return getUserLibrary();
    }
    public HBox getUserHiredBook() {
        if (userHiredBook == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/HiredBook.fxml"));
                userHiredBook = loader.load();
                userBookController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userHiredBook;
    }

    public UserBookController getUserBookController() {
        if(userBookController == null) {
            getUserHiredBook();
        }
        return userBookController;
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Cart.fxml"));
                userCart = loader.load();
                userCartController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userCart;
    }

    public UserCartController getUserCartController() {
        if(userCartController == null) {
            getUserCart();
        }
        return userCartController;
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

    public void setBookDisplayController(UserBookDisplayController bookDisplayController) {
        this.bookDisplayController = bookDisplayController;
    }

    public UserBookDisplayController getBookDisplayController() {
        if(bookDisplayController == null) {
            getBookDetail();
        }
        return bookDisplayController;
    }

    public Node getUserPending() {
        if (userPending == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/UserPending.fxml"));
                userPending = loader.load();
                userPendingController = loader.getController();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userPending;
    }

    public UserPendingController getUserPendingController() {
        if(userPendingController == null) {
            getUserPending();
        }
        return userPendingController;
    }

    public HBox getAdminLibrary() {
        if (adminLibrary == null) {
            try {
                adminLibrary = new FXMLLoader(getClass().getResource("/FXML/AdminLibrary.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminLibrary;
    }

    public HBox getAdminRequestManagement() {
        if (adminRequestManagement == null) {
            try {
                adminRequestManagement = new FXMLLoader(getClass().getResource("/FXML/ManageUserBorrow.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminRequestManagement;
    }

    public HBox getAdminPendingRequestManagement() {
        if (adminPendingRequestManagement == null) {
            try {
                adminPendingRequestManagement = new FXMLLoader(getClass().getResource("/FXML/PendingManagement.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminPendingRequestManagement;
    }

    public HBox getAdminSettings() {
        if (adminSettings == null) {
            try {
                adminSettings = new FXMLLoader(getClass().getResource("/FXML/ManageBookScreen.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminSettings;
    }

    public HBox getAdminDashboard() {
        if (adminDashboard == null) {
            try {
                adminDashboard = new FXMLLoader(getClass().getResource("/FXML/AdminDashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminDashboard;
    }

    public VBox getAdminBookViewing() {
        if (adminBookViewing == null) {
            try {
                adminBookViewing = new FXMLLoader(getClass().getResource("/FXML/BookDisplay.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminBookViewing;
    }


    public void showUserWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/UserView.fxml"));
        UserView user = new UserView();
        fxmlLoader.setController(user);
        createStage(fxmlLoader);
    }

    public void showAdminWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/AdminView.fxml"));
        AdminView adminView = new AdminView();
        fxmlLoader.setController(adminView);
        createStage(fxmlLoader);
    }

    public void showUserDashboard() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/UserDashboard.fxml"));
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

    public StringProperty getSelectedAdminMode() { return selectedAdminMode; }
}
