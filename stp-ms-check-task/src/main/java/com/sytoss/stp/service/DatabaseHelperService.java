package com.sytoss.stp.service;

import bom.QueryResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.SearchPathResourceAccessor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.*;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseHelperService {

    private final QueryResultConvertor queryResultConvertor;

    @Value("${database.url}")
    private String url;

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    @Getter
    private QueryResult queryResult = new QueryResult();

    public void generateDatabase(String databaseScript) {
        Connection conn;
        try {
            url += generateDatabaseName();
            Class.forName("org.h2.Driver");
            conn = DriverManager.getConnection(url, username, password);
            File databaseFile = writeDatabaseScriptFile(databaseScript);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            Liquibase liquibase = new Liquibase(databaseFile.getName(),
                    new SearchPathResourceAccessor("stp-ms-check-task/src/main/resources/scripts"), database);
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
        JsonNode jsonNodeTree = new ObjectMapper().readTree(databaseScript);
        String jsonAsYaml = new YAMLMapper().writeValueAsString(jsonNodeTree).replaceAll("\"", "");
        File scriptFile = File.createTempFile("script", ".yml", new File("stp-ms-check-task/src/main/resources/scripts/"));
        OutputStreamWriter myWriter = new FileWriter(scriptFile);
        myWriter.write(jsonAsYaml);
        myWriter.flush();
        myWriter.close();
        return scriptFile;
    }

    public void getExecuteQueryResult(String answer, String etalon) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(answer);
            queryResultConvertor.convert(resultSet, "answer", queryResult);
            resultSet = statement.executeQuery(etalon);
            queryResultConvertor.convert(resultSet, "etalon", queryResult);
        }
    }
}