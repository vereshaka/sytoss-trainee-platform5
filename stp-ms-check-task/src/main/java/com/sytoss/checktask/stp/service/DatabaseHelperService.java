package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.exceptions.DatabaseCommunicationException;
import com.sytoss.checktask.stp.service.db.Executor;
import com.sytoss.checktask.stp.service.db.PostgresExecutor;
import com.sytoss.domain.bom.checktask.QueryResult;
import liquibase.GlobalConfiguration;
import liquibase.Liquibase;
import liquibase.ScopeManager;
import liquibase.ThreadLocalScopeManager;
import liquibase.command.CommandScope;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.SearchPathResourceAccessor;
import liquibase.ui.LoggerUIService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.Map;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
@Scope(value = "prototype")
@Setter
public class DatabaseHelperService {

    private static final ScopeManager SCOPE_MANAGER = new ThreadLocalScopeManager();

    static {
        liquibase.Scope.setScopeManager(SCOPE_MANAGER);
        try {
            liquibase.Scope.enter(Map.of(liquibase.Scope.Attr.ui.name(), new LoggerUIService()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        try {
            File databaseFile = writeDatabaseScriptFile(databaseScript);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(getConnection()));
            Liquibase liquibase = new Liquibase(databaseFile.getName(),
                    new SearchPathResourceAccessor(databaseFile.getParentFile().getAbsolutePath()), database);
           // cleanThreadLocals();
            liquibase.update();
            ((ThreadLocalScopeManager)SCOPE_MANAGER).remove();
            databaseFile.deleteOnExit();
            log.info("Database was generated. DbName: " + dbName);
        } catch (Exception e) {
            throw new DatabaseCommunicationException("Database creating error", e);
        }
    }

    private void cleanThreadLocals() {
        try {
            Thread thread = Thread.currentThread();
            Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
            threadLocalsField.setAccessible(true);
            Object threadLocalTable = threadLocalsField.get(thread);

            Class threadLocalMapClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");
            Field tableField = threadLocalMapClass.getDeclaredField("table");
            tableField.setAccessible(true);
            Object table = tableField.get(threadLocalTable);

            Field referentField = Reference.class.getDeclaredField("referent");
            referentField.setAccessible(true);

            for (int i = 0; i < CollectionUtils.size(table); i++) {
                Object entry = CollectionUtils.get(table, i);
                if (entry != null) {
                    ThreadLocal threadLocal = (ThreadLocal) referentField.get(entry);
                    threadLocal.remove();
                }
            }
            log.info("Thread Local variables removed. DbName: " + dbName);
        } catch (Exception e) {
            throw new IllegalStateException(e);
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
