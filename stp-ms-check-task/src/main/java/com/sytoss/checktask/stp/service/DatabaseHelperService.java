package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.service.db.Executor;
import com.sytoss.domain.bom.checktask.QueryResult;
import liquibase.Liquibase;
import liquibase.ThreadLocalScopeManager;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionCommandStep;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.SearchPathResourceAccessor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import static org.hsqldb.DatabaseManager.getDatabase;


@Slf4j
@Service
@RequiredArgsConstructor
@Scope(value = "prototype")
@Setter
public class DatabaseHelperService {

    private final static Random DATABASE_GENERATOR = new Random();

    static {
        liquibase.Scope.setScopeManager(new ThreadLocalScopeManager());
    }

    private final QueryResultConvertor queryResultConvertor;
    private final Executor executor;
    private Connection connection;
    @Value("${custom.executor.username}")
    private String username;

    @Value("${custom.executor.password}")
    private String password;

    @Value("${custom.executor.serverPath}")
    private String serverPath;

    private Connection getConnection() {
        if (connection == null) {
            try {
                connection = executor.createConnection(username, password, serverPath, generateDatabaseName());
            } catch (Exception e) {
                throw new CreateDbConnectionException("Could not create connection", e);
            }
        }
        return connection;
    }

    @Synchronized
    public void generateDatabase(String databaseScript) {
        try {
            File databaseFile = writeDatabaseScriptFile(databaseScript);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getConnection()));
            Liquibase liquibase = new Liquibase(databaseFile.getName(),
                    new SearchPathResourceAccessor(databaseFile.getParentFile().getAbsolutePath()), database);
            liquibase.update();
            databaseFile.deleteOnExit();
            log.info(database.getDefaultSchemaName() + " database was generated");
        } catch (Exception e) {
            throw new DatabaseCommunicationException("Database creating error", e);
        }
    }

    public void dropDatabase() {
        try {
            executor.dropDatabase(getConnection());
            log.info("database was dropped");
        } catch (Exception e) {
            log.error("Error in database dropping", e);
        } finally {
            connection = null;
            try {
                if (!getConnection().isClosed()) {
                    getConnection().close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String generateDatabaseName() {
        int databaseNameLength = 25;
        char letter;
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < databaseNameLength; i++) {
            letter = (char) (DATABASE_GENERATOR.nextInt(26) + 'a');
            name.append(letter);
        }
        return "delme" + name.toString();
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
        log.info("Query " + query + " is being executed");
        try (Statement statement = getConnection().createStatement()) {
            if (query != null && !query.trim().toLowerCase().startsWith("select")) {
                int result = statement.executeUpdate(query);
                queryResult.setAffectedRowsCount(result);
                resultSet = statement.executeQuery(checkAnswer);
            } else {
                resultSet = statement.executeQuery(query);
            }
            log.info("Query " + query + " was executed");
            queryResultConvertor.convertFromResultSet(resultSet, queryResult);
        }
        return queryResult;
    }

}
