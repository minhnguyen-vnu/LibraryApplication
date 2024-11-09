module com.jmc.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires mysql.connector.j;
    requires java.desktop;
    requires google.api.services.books.v1.rev114;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires org.json;
    requires commons.logging;

    opens com.jmc.library to javafx.fxml;
    exports com.jmc.library.Controllers;
    exports com.jmc.library;
    exports com.jmc.library.Models;
    exports com.jmc.library.View;
    exports com.jmc.library.Controllers.Users;
    exports com.jmc.library.Assets;
    exports com.jmc.library.Controllers.LibraryControllers;
    exports com.jmc.library.Controllers.Admin;
    exports com.jmc.library.Controllers.GoogleBookAPI;
    exports com.jmc.library.Controllers.Books;
    exports com.jmc.library.Controllers.Interface;
}