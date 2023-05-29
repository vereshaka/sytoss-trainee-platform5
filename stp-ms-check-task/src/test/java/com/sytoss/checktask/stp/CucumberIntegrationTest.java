package com.sytoss.checktask.stp;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@CucumberContextConfiguration
@SpringBootTest(classes = CheckTaskApplication.class)
@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features")
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class CucumberIntegrationTest {

}
