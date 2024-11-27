package com.jmc.library.Controllers.Admin;

import com.jmc.library.Controllers.Image.ImageUtils;
import com.jmc.library.Controllers.Users.User;
import com.jmc.library.Database.DBQuery;
import com.jmc.library.Models.AdminLibraryModel;
import com.jmc.library.Models.Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class ManageUserController implements Initializable {
    public TextField username_txt_fld;
    public TextField full_name_txt_fld;
    public DatePicker date_of_birth_date_picker;
    public ChoiceBox status_choice_box;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTable();
        addBinding();
        showUsers();
        onAction();
    }

    private void addBinding() {
        username_tb_cl.setCellValueFactory(new PropertyValueFactory<>("username"));
        full_name_tb_cl.setCellValueFactory(new PropertyValueFactory<>("name"));
        date_of_birth_tb_cl.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        total_paid_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
        total_borrowed_tb_cl.setCellValueFactory(new PropertyValueFactory<>("totalBorrowed"));
        status_tb_cl.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void setTable() {
        userList = FXCollections.observableArrayList();
        store_tb.setItems(userList);
    }

    private void onAction() {
        reload_btn.setOnAction(actionEvent -> showUsers());
        return_btn.setOnAction(actionEvent -> Model.getInstance().getViewFactory().getSelectedAdminMode().set("Admin Library View"));
    }

    private void showUsers() {
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
                            image, resultSet.getDouble("totalPaid"),
                            resultSet.getInt("totalBorrowed"), resultSet.getString("status"));
                    userList.add(user);
                }
                resultSet.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        Thread thread = new Thread(dbQuery);
        thread.setDaemon(true);
        thread.start();
    }
}
