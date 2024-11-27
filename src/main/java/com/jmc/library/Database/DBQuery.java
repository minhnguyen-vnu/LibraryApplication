package com.jmc.library.Database;

import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Task for querying the database.
 */
public class DBQuery extends Task<ResultSet> {
    private final String query;
    private final Object[] params;

    /**
     * Creates a new DBQuery task.
     *
     * @param query  the query to execute
     * @param params the parameters to pass to the query
     */
    public DBQuery(String query, Object... params) {
        this.query = query;
        this.params = params;
    }

    @Override
    protected ResultSet call() throws Exception {
        Connection con = DBUtlis.getConnection();
        PreparedStatement preparedStatement = con.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setObject(i + 1, params[i]);
        }
        return preparedStatement.executeQuery();
    }
}