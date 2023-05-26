package com.sytoss.checktask.stp.service;

import bom.QueryResult;
import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationError;
import liquibase.Liquibase;
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

    private static final String username = "SA";

    private static final String password = "~";

    private final QueryResultConvertor queryResultConvertor;

    private String url = "jdbc:h2:~/";

    public void generateDatabase(String databaseScript) {
        url += generateDatabaseName();
        try (Connection connection = DriverManager.getConnection(url, username, password)){
            Class.forName("org.h2.Driver");
            File databaseFile = writeDatabaseScriptFile(databaseScript);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(databaseFile.getName(),
                    new SearchPathResourceAccessor(databaseFile.getParentFile().getAbsolutePath()), database);
            liquibase.update();
            databaseFile.deleteOnExit();
        } catch (Exception e) {
            throw new DatabaseCommunicationError("Database creating error",e);
        }
    }

    public void executeQuery(String sqlQuery) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeQuery(sqlQuery);
            log.info("Query was executed");
        } catch (Exception e) {
            log.error("Error occurred during execution query: {}", sqlQuery);
            log.error("Error: ", e);
            throw new DatabaseCommunicationError("Error during query execution",e);
        }
    }

    public void dropDatabase() {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP ALL OBJECTS DELETE FILES;");
            log.info("database was dropped");
        } catch (SQLException e) {
            throw new DatabaseCommunicationError("Error in database dropping",e);
        }
    }

    private String generateDatabaseName() {
        int databaseNameLength = 30;
        Random r = new Random();
        char letter;
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < databaseNameLength; i++) {
            letter = (char) (r.nextInt(26) + 'a');
            name.append(letter);
        }
        return name.toString();
    }

    private File writeDatabaseScriptFile(String databaseScript) throws IOException {
        File scriptFile = File.createTempFile("script", ".yml");
        try( OutputStreamWriter myWriter = new FileWriter(scriptFile)) {
            myWriter.write(databaseScript);
            myWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return scriptFile;
    }

    public QueryResult getExecuteQueryResult(String query) {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            return new QueryResult(queryResultConvertor.convertFromResultSet(resultSet));
        } catch (Exception e) {
            throw new DatabaseCommunicationError("Error during the receiving execute query result",e);
        }
    }
}