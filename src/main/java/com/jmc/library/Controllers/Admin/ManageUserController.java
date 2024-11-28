package com.jmc.library.Controllers.Admin;

import com.jmc.library.Assets.RequestInfo;
import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Controllers.Users.User;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller class for managing the users in the library.
 */
public class ManageUserController implements Initializable {
    public TextField username_txt_fld;
    public TextField full_name_txt_fld;
    public DatePicker date_of_birth_date_picker;
    public ChoiceBox<String> status_choice_box;
    public Button search_btn;
    public Button return_btn;
    public Button reload_btn;

    public TableView<User> store_tb;
    public ObservableList<User> userList;
    public TableColumn<User, String> username_tb_cl;
    public TableColumn<User, String> full_name_tb_cl;
    public TableColumn<User, LocalDate> date_of_birth_tb_cl;
    public TableColumn<User, Double> total_paid_tb_cl;
    public TableColumn<User, Integer> total_borrowed_tb_cl;
    public TableColumn<User, String> status_tb_cl;
    public Label noiti_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable();
        addBinding();
        onAction();
        ChoiceBoxInitialization();
        showUsers();
    }

    /**
     * Adds the loading placeholder to the table.
     */
    protected void addLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/Loading.fxml"));
        try {
            ImageView loading_img = loader.load();
            store_tb.setPlaceholder(loading_img);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds the no data placeholder to the table.
     */
    protected void returnLoading() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/NoDataPlaceHolder.fxml"));
        try {
            Label label = loader.load();
            store_tb.setPlaceholder(label);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sets up the table with the list of users.
     */
    private void addBinding() {
        username_tb_cl.setCellValueFactory(new PropertyValueFactory<>("username"));
        full_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("name"));
        date_of_birth_tb_cl.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        total_paid_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
        total_borrowed_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalBorrowed"));
        status_tb_cl.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    /**
     * Sets up the table with the list of users.
     */
    private void setTable() {
        userList = FXCollections.observableArrayList();
        store_tb.setItems(userList);
    }

    /**
     * Sets the action to be executed when the button is clicked.
     */
    private void onAction() {
        reload_btn.setOnAction(actionEvent -> showUsers());
        return_btn.setOnAction(actionEvent -> {
            username_txt_fld.clear();
            full_name_txt_fld.clear();
            date_of_birth_date_picker.setValue(null);
            store_tb.setItems(userList);
            Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View");
        });
        search_btn.setOnAction(actionEvent -> search());
    }

    private void ChoiceBoxInitialization() {
        status_choice_box.setItems(FXCollections.observableArrayList("online", "offline", ""));
    }

    private void search() {
        String username = username_txt_fld.getText();
        String fullname = full_name_txt_fld.getText();
        LocalDate doB = date_of_birth_date_picker.getValue();
        String status = status_choice_box.getValue();

        ObservableList<User> filteredList = userList.stream()
                .filter(request ->
                        (username.isEmpty() || request.getUsername().equals(username)) &&
                                (fullname.isEmpty() || request.getName().equals(fullname)) &&
                                (doB == null || request.getBirthDate().equals(doB)) &&
                                (status == null || status.isEmpty() || request.getStatus().equals(status)))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        store_tb.setItems(filteredList);
    }

    /**
     * Shows the list of users in the library.
     */
    private void showUsers() {
        addLoading();
        userList.clear();
        store_tb.setItems(userList);
        DBQuery dbQuery = new DBQuery("select\n" +
                "    us.username,\n" +
                "    us.password,\n" +
                "    us.name,\n" +
                "    us.birthdate,\n" +
                "    us.ID,\n" +
                "    us.imageView,\n" +
                "    us.status,\n" +
                "    coalesce(count(bookName), 0) totalBorrowed,\n" +
                "    coalesce(sum(cost), 0) totalPaid\n" +
                "from users us\n" +
                "left join userRequest ur using(username)\n" +
                "group by us.username, us.password, us.name, us.birthdate, us.ID, us.imageView, us.status;");
        dbQuery.setOnSucceeded(event -> {
            ResultSet resultSet = dbQuery.getValue();
            try {
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("username"));
                    Blob blob = resultSet.getBlob("imageView");
                    Image image = null;
                    if (blob != null && blob.length() > 1 ) {
                        byte[] imageBytes = blob.getBytes(1, (int) blob.length());
                        image = ImageUtils.byteArrayToImage(imageBytes);
                    } else {
                        image = new ImageView(getClass().getResource("/IMAGES/UnknownBookCover.png").toExternalForm()).getImage();
                    }
                    LocalDate birthdate = null;
                    if (resultSet.getDate("birthdate") != null) {
                        birthdate = resultSet.getDate("birthdate").toLocalDate();
                    } else {
                        birthdate = LocalDate.of(1970, 1, 1); // Default date
                    }
                    String name = resultSet.getString("name");
                    if (name == null) {
                        name = "N/A"; // Default name
                    }
                    int id = resultSet.getInt("ID");
                    if (resultSet.wasNull()) {
                        id = 0; // Default ID
                    }
                    User user = new User(resultSet.getString("username"), resultSet.getString("password"),
                            name, birthdate, id,
                            image, Math.round(resultSet.getDouble("totalPaid") * 100.0 )/100.0,
                            resultSet.getInt("totalBorrowed"), resultSet.getString("status"));
                    userList.add(user);
                }
                resultSet.close();
                returnLoading();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }
}
