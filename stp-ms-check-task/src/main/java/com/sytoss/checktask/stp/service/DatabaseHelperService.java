package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.QueryResult;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.SearchPathResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class DatabaseHelperService {

    private final QueryResultConvertor queryResultConvertor;

    private static final String username = "SA";

    private static final String password = "~";

    private String url = "jdbc:h2:~/";

    public void generateDatabase(String databaseScript) {
        Connection conn;
        try {
            url += generateDatabaseName();
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(url, username, password);
            File databaseFile = writeDatabaseScriptFile(databaseScript);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(databaseFile.getName(),
                    new SearchPathResourceAccessor(databaseFile.getParentFile().getAbsolutePath()), database);
            liquibase.update();
            databaseFile.deleteOnExit();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeQuery(String sqlQuery) throws Exception {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sqlQuery);
            log.info("Query was executed");
        } catch (Exception e) {
            log.error("Error occurred during execution query: {}", sqlQuery);
            log.error("Error: ", e);
            throw e;
        }
    }

    public void dropDatabase() throws Exception {
        executeQuery("DROP ALL OBJECTS DELETE FILES;");
        log.info("database was dropped");
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
        OutputStreamWriter myWriter = new FileWriter(scriptFile);
        myWriter.write(databaseScript);
        myWriter.flush();
        myWriter.close();
        return scriptFile;
    }

    public QueryResult getExecuteQueryResult(String answer, String etalon) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(answer);
            QueryResult queryResult = new QueryResult();
            queryResult.setAnswer(queryResultConvertor.convertFromResultSet(resultSet));
            resultSet = statement.executeQuery(etalon);
            queryResult.setEtalon(queryResultConvertor.convertFromResultSet(resultSet));
            resultSet.close();
            statement.close();
            return queryResult;
        }
    }
}