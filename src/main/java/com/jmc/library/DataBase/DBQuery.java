package com.jmc.library.DataBase;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBQuery extends Task<ResultSet> {
    private final String query;
    private final Object[] params;

    public DBQuery(String query, Object... params) {
        this.query = query;
        this.params = params;
    }

    @Override protected ResultSet call() throws Exception {
        Connection con = DBUtlis.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }
}