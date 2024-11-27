package com.jmc.library.View;

import com.jmc.library.Controllers.Admin.AdminView;
import com.jmc.library.Controllers.Authentication;
import com.jmc.library.Controllers.Users.UserView;
import com.jmc.library.Database.DBUtlis;
import com.jmc.library.Models.LibraryModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

/**
 * Factory class for creating views.
 */
public class ViewFactory {
    private HBox logInView;
    private HBox signUpView;
    private HBox userLibrary;
    private HBox userBorrowedBook;
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
    private HBox adminUserManagement;
    private final StringProperty selectedAuthenticatonMode;
    private final StringProperty selectedUserMode;
    private final StringProperty selectedAdminMode;


    /**
     * Constructs a ViewFactory and initializes the mode properties.
     */
    public ViewFactory() {
        this.selectedAuthenticatonMode = new SimpleStringProperty("");
        this.selectedUserMode = new SimpleStringProperty("");
        this.selectedAdminMode = new SimpleStringProperty("");
    }

    /**
     * Shows the authentication window.
     */
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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Library.fxml"));
                userLibrary = loader.load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userLibrary;
    }

    public HBox getUserStore() {
        return getUserLibrary();
    }

    public HBox getUserBorrowedBook() {
        if (userBorrowedBook == null) {
            try {
                userBorrowedBook = new FXMLLoader(getClass().getResource("/FXML/BorrowedBook.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userBorrowedBook;
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

    public HBox getAdminUserManagement() {
        if (adminUserManagement == null) {
            try {
                adminUserManagement = new FXMLLoader(getClass().getResource("/FXML/UserManagement.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return adminUserManagement;
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

    /**
     * Shows the user window.
     */
    public void showUserWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/UserView.fxml"));
        UserView user = new UserView();
        fxmlLoader.setController(user);
        createStage(fxmlLoader);
    }

    /**
     * Shows the admin window.
     */
    public void showAdminWindow() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FXML/AdminView.fxml"));
        AdminView adminView = new AdminView();
        fxmlLoader.setController(adminView);
        createStage(fxmlLoader);
    }

    /**
     * Creates a new stage with the specified loader.
     *
     * @param loader The FXMLLoader to use for creating the stage.
     */
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
        stage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, windowEvent -> {
            System.out.println(LibraryModel.getInstance().getUser().getUsername());
            DBUtlis.executeUpdate("update users\n" +
                    "set status = ?\n" +
                    "where username = ?;", "offline", LibraryModel.getInstance().getUser().getUsername());
        });
    }

    /**
     * Creates a new stage with the specified parent.
     *
     * @param loader The Parent to use for creating the stage.
     */
    void createStage(Parent loader) {
        Scene scene = new Scene(loader);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Library Application");
        stage.show();
    }

    /**
     * Closes the specified stage.
     *
     * @param stage The stage to close.
     */
    public void closeStage(Stage stage) {
        stage.close();
    }

    /**
     * Resets all views to null.
     */
    public void resetAll() {
        System.out.println("ViewFactory.resetAll");
        logInView = null;
        signUpView = null;
        userLibrary = null;
        userBorrowedBook = null;
        userDashboard = null;
        userCart = null;
        userPending = null;
        bookDetail = null;
        notificationView = null;
    }

    public StringProperty getSelectedAdminMode() {
        return selectedAdminMode;
    }
}
