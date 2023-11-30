package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.domain.bom.checktask.QueryResult;
import liquibase.Liquibase;
import liquibase.ThreadLocalScopeManager;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.SearchPathResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
@Scope(value = "prototype")
public class DatabaseHelperService {

    static {
        liquibase.Scope.setScopeManager(new ThreadLocalScopeManager());
    }

    private static final String username = "SA";

    private static final String password = "~";

    private static final String ORACLE_MODE = "MODE=Oracle";
    private static final String MSSQL_MODE = "MODE=MSSQLServer";

    private final QueryResultConvertor queryResultConvertor;

    private Connection connection;

    private final static Random DATABASE_GENERATOR = new Random();

    private Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.h2.Driver");
                String url = "jdbc:h2:mem:" + generateDatabaseName() + ";" + MSSQL_MODE;
                connection = DriverManager.getConnection(url, username, password);
            } catch (Exception e) {
                throw new CreateDbConnectionException("Could not create connection", e);
            }
        }
        return connection;
    }

    public void generateDatabase(String databaseScript) {
        try {
            File databaseFile = writeDatabaseScriptFile(databaseScript);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getConnection()));
            Liquibase liquibase = new Liquibase(databaseFile.getName(),
                    new SearchPathResourceAccessor(databaseFile.getParentFile().getAbsolutePath()), database);
            liquibase.update();
            databaseFile.deleteOnExit();
            log.info("database was generated");
        } catch (Exception e) {
            throw new DatabaseCommunicationException("Database creating error", e);
        }
    }

    public void dropDatabase() {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate("DROP ALL OBJECTS DELETE FILES;");
            log.info("database was dropped");
        } catch (Exception e) {
            log.error("Error in database dropping", e);
        } finally {
            try {
                getConnection().close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            connection = null;
        }
    }

    private String generateDatabaseName() {
        int databaseNameLength = 30;

        char letter;
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < databaseNameLength; i++) {
            letter = (char) (DATABASE_GENERATOR.nextInt(26) + 'a');
            name.append(letter);
        }
        return name.toString();
    }

    private File writeDatabaseScriptFile(String databaseScript) throws IOException {
        File scriptFile = File.createTempFile("script", ".yml");
        try (OutputStreamWriter myWriter = new FileWriter(scriptFile)) {
            myWriter.write(databaseScript);
            myWriter.flush();
        } catch (IOException e) {
            throw new DatabaseCommunicationException("Error during work with a database", e);
        }
        return scriptFile;
    }

    public QueryResult getExecuteQueryResult(String query, String checkAnswer) throws SQLException {
        QueryResult queryResult = new QueryResult();
        ResultSet resultSet;
        try (Statement statement = getConnection().createStatement()) {
            if(query!=null && !query.toLowerCase().startsWith("select")){
                int result = statement.executeUpdate(query);
                queryResult.setAffectedRowsCount(result);
                resultSet = statement.executeQuery(checkAnswer);
            }else{
                resultSet = statement.executeQuery(query);
            }

            queryResultConvertor.convertFromResultSet(resultSet,queryResult);
        }
        return queryResult;
    }

}
