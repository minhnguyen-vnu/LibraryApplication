module com.jmc.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;

    opens com.jmc.library to javafx.fxml;
    exports com.jmc.library.Controllers;
    exports com.jmc.library;
    exports com.jmc.library.Models;
    exports com.jmc.library.View;
    exports com.jmc.library.Controllers.Users;
    exports com.jmc.library.Controllers.Admin;
}