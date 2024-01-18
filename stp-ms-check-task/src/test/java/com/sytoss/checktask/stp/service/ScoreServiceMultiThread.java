package com.sytoss.checktask.stp.service;

import com.sytoss.checktask.stp.service.db.PostgresExecutor;
import com.sytoss.domain.bom.convertors.PumlConvertor;
import com.sytoss.domain.bom.personalexam.CheckTaskParameters;
import com.sytoss.domain.bom.personalexam.Score;
import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static com.sytoss.stp.test.FileUtils.readFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
@Disabled
public class ScoreServiceMultiThread {

    private static final int threadCount = 50;

    private static final String dbScript = IOUtils.toString(DatabaseHelperMultiThread.class.getResourceAsStream("/data/task-domain/prod-trade23-db.yml"));

    private static final String dataScript = IOUtils.toString(DatabaseHelperMultiThread.class.getResourceAsStream("/data/task-domain/prod-trade23-data.yml"));

    private static final String script = new PumlConvertor().convertToLiquibase(dbScript + "\n\n" + dataScript);

    @Autowired
    private ScoreService scoreService;

    private static int cnt = 0;

    @Test
    public void runMultipleCheckScore() {
        assertNotNull(scoreService);

        CheckTaskParameters checkTaskParameters = new CheckTaskParameters();
        checkTaskParameters.setRequest("select * from Client");
        checkTaskParameters.setEtalon("select * from Client");
        checkTaskParameters.setScript(script);

        List<Thread> creaters = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            creaters.add(new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Score result = scoreService.checkAndScore(checkTaskParameters);
                        assertEquals(1.0, result.getValue());
                        cnt++;
                    } catch (Exception e) {
                        log.error("Failed. Message: " + e.getMessage());
                    }
                }
            }));
        }
        creaters.forEach(item -> item.start());
        creaters.forEach(item -> {
            try {
                item.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        assertEquals(threadCount, cnt);
    }
}
