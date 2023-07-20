package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.RequestIsNotValidException;
import com.sytoss.domain.bom.lessons.QueryResult;
import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
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
    private String url="";

    public void generateDatabase(String startUrl,String databaseScript) {
        url += startUrl + generateDatabaseName();
        if(startUrl.contains("mem")){
            url += ";MODE=Oracle";
        }
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Class.forName("org.h2.Driver");
            File databaseFile = writeDatabaseScriptFile(databaseScript);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
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
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP ALL OBJECTS DELETE FILES;");
            log.info("database was dropped");
        } catch (SQLException e) {
            log.error("Error in database dropping", e);
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
        try (OutputStreamWriter myWriter = new FileWriter(scriptFile)) {
            myWriter.write(databaseScript);
            myWriter.flush();
        } catch (IOException e) {
            throw new DatabaseCommunicationException("Error during work with a database", e);
        }
        return scriptFile;
    }

    public QueryResult getExecuteQueryResult(String query) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            log.info("query result was got");
            return new QueryResult(queryResultConvertor.convertFromResultSet(resultSet));
        }
    }
}