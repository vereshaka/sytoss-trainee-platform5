package com.sytoss.checktask.stp.service.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class HsqlExecutor implements Executor {

    public Connection createConnection(String username, String password, String serverPath, String dbName) throws ClassNotFoundException, SQLException {
        Class.forName(getDriverName());
        return DriverManager.getConnection(getJdbcUrl(dbName), username, password);
    }

    @Override
    public void dropDatabase(Connection connection, String dbName) {

    }

    @Override
    public String getJdbcUrl(String dbName) {
        return "jdbc:hsqldb:mem:" + dbName + ";DB_CLOSE_DELAY=-1";
    }

    @Override
    public String getDriverName() {
        return "org.hsqldb.jdbc.JDBCDriver";
    }
}
