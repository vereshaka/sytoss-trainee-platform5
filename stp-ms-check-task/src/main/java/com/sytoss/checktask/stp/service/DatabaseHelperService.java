package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.service.db.Executor;
import com.sytoss.domain.bom.checktask.QueryResult;
import liquibase.*;
import liquibase.command.CommandScope;
import liquibase.resource.SearchPathResourceAccessor;
import liquibase.ui.LoggerUIService;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
@Scope(value = "prototype")
@Setter
public class DatabaseHelperService {

    public static final ThreadLocalScopeManager SCOPE_MANAGER = createThreadLocalScopeManager();

    private static ThreadLocalScopeManager createThreadLocalScopeManager() {
        try {
            liquibase.Scope.enter(Map.of(liquibase.Scope.Attr.ui.name(), new LoggerUIService()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ThreadLocalScopeManager threadLocalScopeManager = new ThreadLocalScopeManager();
        liquibase.Scope.setScopeManager(threadLocalScopeManager);
        return threadLocalScopeManager;
    }

    private static int NAME_COUNTER = 0;

    private final QueryResultConvertor queryResultConvertor;

    private Connection connection;

    private final static Random DATABASE_GENERATOR = new Random();

    private final Executor executor;

    @Value("${custom.executor.username}")
    private String username;

    @Value("${custom.executor.password}")
    private String password;

    @Value("${custom.executor.serverPath}")
    private String serverPath;

    private String dbName;

    private Connection getConnection() {
        if (connection == null) {
            try {
                connection = executor.createConnection(username, password, serverPath, dbName = generateDatabaseName());
                log.info("Database created. DbName: " + dbName);
            } catch (Exception e) {
                throw new CreateDbConnectionException("Could not create connection", e);
            }
        }
        return connection;
    }

    public void generateDatabase(String databaseScript) {
        File databaseFile = null;
        try {
            getConnection(); // YevgenyV: this is required code for initialization of schema
            databaseFile = writeDatabaseScriptFile(databaseScript);
            Map<String, Object> values = new HashMap<>();
            values.put(liquibase.Scope.Attr.resourceAccessor.name(), new SearchPathResourceAccessor(databaseFile.getParentFile().getAbsolutePath()));
            values.put(liquibase.Scope.Attr.ui.name(), new LoggerUIService());
            CustomScope scope = new CustomScope(SCOPE_MANAGER.getRootScope(), values);
            SCOPE_MANAGER.setCurrentScope(scope);
            new CommandScope("update")
                    .addArgumentValue("changeLogFile", databaseFile.getName())
                    .addArgumentValue("url", executor.getJdbcUrl(dbName))
                    .addArgumentValue("driver", executor.getDriverName())
                    .addArgumentValue("username", username)
                    .addArgumentValue("password", password)
                    .execute();
            log.info("Database was generated. DbName: " + dbName);
        } catch (Exception e) {
            throw new DatabaseCommunicationException("Database creating error", e);
        } finally {
            SCOPE_MANAGER.remove();
            // cleanThreadLocals();
            if (databaseFile != null) {
                databaseFile.deleteOnExit();
            }
        }
    }

    private void cleanThreadLocals() {
        try {
            Thread thread = Thread.currentThread();
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            threadLocalsField.set(thread, null);
            log.info("Thread Local variables removed. DbName: " + dbName);
        } catch (Exception e) {
            log.warn("Thread Local variables could not be removed. DbName: " + dbName, e);
        }
    }

    public void dropDatabase() {
        try {
            executor.dropDatabase(getConnection(), dbName);
            log.info("Database was dropped. DbName: " + dbName);//, new RuntimeException());
        } catch (Exception e) {
            log.error("Error in database dropping", e);
        } finally {
            try {
                if (!getConnection().isClosed()) {
                    getConnection().close();
                }
            } catch (SQLException e) {
                log.warn("Could not drop database. DbName: " + dbName, e);
            }
            connection = null;
        }
    }

    private String generateDatabaseName() {
        int databaseNameLength = 10;
        char letter;
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < databaseNameLength; i++) {
            letter = (char) (DATABASE_GENERATOR.nextInt(26) + 'a');
            name.append(letter);
        }
        return "delme_" + (NAME_COUNTER++) + "_" + name; // TODO: yevgenyv-tmp: name.toString();
    }

    private File writeDatabaseScriptFile(String databaseScript) throws IOException {
        File scriptFile = File.createTempFile(dbName, ".yml");
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
            if (query != null && !query.trim().toLowerCase().startsWith("select")) {
                int result = statement.executeUpdate(query);
                queryResult.setAffectedRowsCount(result);
                resultSet = statement.executeQuery(checkAnswer);
            } else {
                resultSet = statement.executeQuery(query);
            }

            queryResultConvertor.convertFromResultSet(resultSet, queryResult);
        }
        log.info("Query executed. DbName: " + dbName);
        return queryResult;
    }

}
