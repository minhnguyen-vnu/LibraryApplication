package com.jmc.library.Database;

import java.sql.*;

/**
 * Utility class for database operations.
 */
public class DBUtlis {
    static String User = "admin";
    static String password = "minhcbg123";
    static String url = "jdbc:mysql://database-1.c3me0eu06bvo.ap-southeast-2.rds.amazonaws.com:3306/login";
    static String driver = "com.mysql.cj.jdbc.Driver";

    /**
     * Gets a connection to the database.
     *
     * @return the connection
     */
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

    /**
     * Closes the resources.
     *
     * @param preparedStatement the prepared statement
     * @param resultSet         the result set
     * @param con               the connection
     */
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

    /**
     * Executes an update query.
     *
     * @param query  the query
     * @param params the parameters
     */
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

    /**
     * Executes a query.
     *
     * @param query  the query
     * @param params the parameters
     * @return the result set
     * @throws SQLException if an error occurs
     */
    public static ResultSet executeQuery(String query, Object... params) throws SQLException {
        Connection con = getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }
}