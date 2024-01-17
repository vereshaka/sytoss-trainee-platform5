package com.sytoss.checktask.stp.service.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HsqlExecutor implements Executor {

    public Connection createConnection(String username, String password, String serverPath, String dbName) throws ClassNotFoundException, SQLException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        String url = "jdbc:hsqldb:mem:" + dbName + ";DB_CLOSE_DELAY=-1";
        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public void dropDatabase(Connection connection, String dbName) {

    }
}
