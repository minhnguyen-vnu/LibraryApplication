package com.jmc.library;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtlis {
    static String User = "admin";
    static String password = "minhcbg123";
    static String url = "jdbc:mysql://database-1.c3me0eu06bvo.ap-southeast-2.rds.amazonaws.com:3306/login";
    static String driver = "com.mysql.cj.jdbc.Driver";

    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName(driver);
            con = DriverManager.getConnection(url, User, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}
