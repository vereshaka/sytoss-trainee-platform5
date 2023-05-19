package com.sytoss.stp.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class DatabaseHelperService {

    private String url = " jdbc:h2:mem:";

    @Value("${database.username}")
    private String username;

    @Value("${database.password}")
    private String password;

    public Connection generateDatabase(String databaseScript) throws Exception {
        try {
            Class.forName("org.h2.Driver");
            String name = generateDatabaseName();
            url += name;
            Connection connection = DriverManager.getConnection(url, username, password);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase(databaseScript, new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            log.info("Database with the name" + name + "was created");
            return connection;
        } catch (Exception e) {
            log.error("Error occurred during generating database", e);
            throw e;
        }
    }

    public void executeQuery(String sqlQuery) throws Exception {
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement()) {
            statement.executeQuery(sqlQuery);
        } catch (Exception e) {
            log.error("Error occurred during execution query: {}", sqlQuery);
            log.error("Error: ", e);
            throw e;
        }
    }

    public void dropDatabase() throws Exception {
        executeQuery("DROP ALL OBJECTS DELETE FILES;");
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
}
