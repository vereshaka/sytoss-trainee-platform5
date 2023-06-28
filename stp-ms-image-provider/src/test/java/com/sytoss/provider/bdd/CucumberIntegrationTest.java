package com.sytoss.provider.bdd;

import com.sytoss.provider.connector.ImageConnector;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class CucumberIntegrationTest extends StpIntegrationTest {

    @Autowired
    private ImageConnector imageConnector;
}