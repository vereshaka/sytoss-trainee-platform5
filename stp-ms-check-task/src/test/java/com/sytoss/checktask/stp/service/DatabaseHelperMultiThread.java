package com.sytoss.checktask.stp.service;

import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.stp.test.StpUnitTest;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

public class DatabaseHelperMultiThread extends StpUnitTest {

    private static final int threadCount = 100;

    private static final String dbScript = IOUtils.toString(DatabaseHelperMultiThread.class.getResourceAsStream("/data/task-domain/prod-trade23-db.yml"));
    private static final String dataScript = IOUtils.toString(DatabaseHelperMultiThread.class.getResourceAsStream("/data/task-domain/prod-trade23-data.yml"));

    private static final String script = new PumlConvertor().convertToLiquibase(dbScript + "\n\n" + dataScript);

    @Test
    public void runDBCreation() {
        LogManager.getLogManager().reset();
        List<Thread> creaters = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            creaters.add(new Thread(new DBCreater()));
        }
        creaters.forEach(item -> item.start());
        creaters.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    class DBCreater implements Runnable {

        private DatabaseHelperService service = new DatabaseHelperService(new QueryResultConvertor());

        @Override
        public void run() {
            service.generateDatabase(script);
        }
    }
}
