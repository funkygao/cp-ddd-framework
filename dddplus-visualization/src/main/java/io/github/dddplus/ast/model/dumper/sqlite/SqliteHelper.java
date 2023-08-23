/*
 * Copyright DDDplus Authors.
 *
 * Licensed under the Apache License version 2.0, available at http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.dddplus.ast.model.dumper.sqlite;

import java.sql.*;
import java.util.List;

class SqliteHelper {
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String dbFilePath;

    SqliteHelper(String dbFilePath) throws ClassNotFoundException, SQLException {
        this.dbFilePath = dbFilePath;
        this.connection = getConnection(dbFilePath);
    }

    private Connection getConnection(String dbFilePath) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
    }

    int executeUpdate(String sql) throws SQLException, ClassNotFoundException {
        try {
            return getStatement().executeUpdate(sql);
        } finally {
            destroyed();
        }
    }

    void executeUpdate(List<String> sqls) throws SQLException, ClassNotFoundException {
        try {
            for (String sql : sqls) {
                getStatement().executeUpdate(sql);
            }
        } finally {
            destroyed();
        }
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        if (null == connection) {
            connection = getConnection(dbFilePath);
        }
        return connection;
    }

    private Statement getStatement() throws SQLException, ClassNotFoundException {
        if (null == statement) {
            statement = getConnection().createStatement();
        }
        return statement;
    }

    void destroyed() throws SQLException {
        if (null != connection) {
            connection.close();
            connection = null;
        }

        if (null != statement) {
            statement.close();
            statement = null;
        }

        if (null != resultSet) {
            resultSet.close();
            resultSet = null;
        }
    }
}
