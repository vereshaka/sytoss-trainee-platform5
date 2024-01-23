package com.sytoss.checktask.stp.service.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface Executor {

    Connection createConnection(String username, String password, String serverPath, String dbName) throws ClassNotFoundException, SQLException;

    void dropDatabase(Connection connection, String dbName) throws SQLException;

    String getJdbcUrl(String dbName);

    String getDriverName();
}
