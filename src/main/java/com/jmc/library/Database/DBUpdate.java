package com.jmc.library.Database;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUpdate extends Task<Void> {
    private final String query;
    private final Object[] params;

    public DBUpdate(String query, Object... params) {
        this.query = query;
        this.params = params;
    }

    @Override protected Void call() throws Exception {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = DBUtlis.getConnection();
            preparedStatement = con.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } finally {
            DBUtlis.closeResources(preparedStatement, null, con);
        }

        return null;
    }
}
