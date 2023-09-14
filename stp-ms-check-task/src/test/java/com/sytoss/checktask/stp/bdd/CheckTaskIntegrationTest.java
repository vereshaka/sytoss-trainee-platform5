package com.sytoss.checktask.stp.bdd;

import com.sytoss.checktask.stp.bdd.common.CheckTaskDetails;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@CucumberContextConfiguration
@ExtendWith(SpringExtension.class)
public class CheckTaskIntegrationTest extends StpIntegrationTest<CheckTaskDetails> {

    @Override
    protected CheckTaskDetails createDetails() {
        return new CheckTaskDetails();
    }
}
