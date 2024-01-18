package com.sytoss.checktask.stp;

import com.sytoss.checktask.stp.service.db.Executor;
import com.sytoss.checktask.stp.service.db.H2Executor;
import com.sytoss.checktask.stp.service.db.HsqlExecutor;
import com.sytoss.checktask.stp.service.db.PostgresExecutor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class CheckTaskConfiguration {

    @Value("${custom.executor.executorType}")
    private String executorType;

    @Value("#{new Integer(${custom.executor.poolSize})}")
    private int poolSize;


    @Bean
    public Executor createExecutor() {
        switch (executorType) {
            case "H2":
                return new H2Executor();
            case "HSQL":
                return new HsqlExecutor();
            case "POSTGRESQL":
                return new PostgresExecutor();
            default:
                throw new RuntimeException("Unknown value of executor type: " + executorType);
        }
    }

    @Bean
    public ExecutorService createThreadPool(){
        return Executors.newFixedThreadPool(poolSize);
    }
}
