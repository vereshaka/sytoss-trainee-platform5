package com.sytoss.checktask.stp.service.db;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class PostgresExecutor implements Executor {

    private String username;
    private String password;
    private String serverPath;
    private String dbName;

    public Connection createConnection(String username, String password, String serverPath, String dbName) throws ClassNotFoundException, SQLException {
        this.username = username;
        this.password = password;
        this.serverPath = serverPath;
        this.dbName = dbName;

        Class.forName("org.postgresql.Driver");
        Connection adminConnection = null;
        Statement statement = null;
        try {
            adminConnection = DriverManager.getConnection("jdbc:postgresql://" + serverPath, username, password);
            statement = adminConnection.createStatement();
            statement.execute("create schema " + dbName);
            log.info("Schema created: " + dbName);
        } catch (SQLException e) {
            log.warn("create connection error", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                log.warn("close schema statement error", e);
            }
            try {
                if (adminConnection != null) {
                    adminConnection.close();
                }
            } catch (Exception e) {
                log.warn("close admin connection error", e);
            }
        }
        String url = "jdbc:postgresql://" + serverPath + "?currentSchema=" + dbName;
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public void dropDatabase(Connection connection) throws SQLException {
        connection.close();
        String url = "jdbc:postgresql://" + serverPath + "?currentSchema=" + dbName;
        Connection adminConnection = null;
        Statement statement = null;
        try {
            adminConnection = DriverManager.getConnection(url, username, password);
            statement = adminConnection.createStatement();
            statement.execute("drop schema " + dbName + " cascade");
            statement.close();
            log.info("Schema dropped: " + dbName);
        } catch (SQLException e) {
            log.warn("drop schema error", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (Exception e) {
                log.warn("close schema statement error", e);
            }
            try {
                if (adminConnection != null) {
                    adminConnection.close();
                }
            } catch (Exception e) {
                log.warn("close admin connection error", e);
            }
        }
    }
}
