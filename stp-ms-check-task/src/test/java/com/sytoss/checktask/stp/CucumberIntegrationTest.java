package com.sytoss.checktask.stp;

import com.sytoss.checktask.stp.service.DatabaseHelperService;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.mock;

@CucumberContextConfiguration
@SpringBootTest(classes = CheckTaskApplication.class)
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty", "json:target/cucumber-report.json"}, features = "src/test/resources/features")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CucumberIntegrationTest {
    protected static final ThreadLocal<DatabaseHelperService> databaseHelperService =
            ThreadLocal.withInitial(() -> mock(DatabaseHelperService.class));
}
