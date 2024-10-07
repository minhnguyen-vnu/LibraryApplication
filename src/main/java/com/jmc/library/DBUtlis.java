package com.jmc.library;

import java.sql.*;

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

    public static void closeResources(PreparedStatement preparedStatement, ResultSet resultSet, Connection con) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void executeUpdate(String query, Object... params) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = getConnection();
            try {
                preparedStatement = con.prepareStatement(query);
                for (int i = 0; i < params.length; i++) {
                    preparedStatement.setObject(i + 1, params[i]);
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } finally {
            closeResources(preparedStatement, null, con);
        }
    }

    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }
}
