package com.sytoss.checktask.stp.service.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Executor implements Executor {

    private static final String ORACLE_MODE = "MODE=Oracle";
    private static final String MSSQL_MODE = "MODE=MSSQLServer";

    public Connection createConnection(String username, String password, String serverPath, String dbName) throws ClassNotFoundException, SQLException {
        Class.forName(getDriverName());
        return DriverManager.getConnection(getJdbcUrl(dbName), username, password);
    }

    @Override
    public void dropDatabase(Connection connection, String dbName) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DROP ALL OBJECTS DELETE FILES");
        statement.close();
    }

    @Override
    public String getJdbcUrl(String dbName) {
        return "jdbc:h2:mem:" + dbName + ";" + MSSQL_MODE;
    }

    @Override
    public String getDriverName() {
        return "org.h2.Driver";
    }
}
