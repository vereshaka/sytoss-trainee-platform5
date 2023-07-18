package com.sytoss.provider.bdd;

import com.sytoss.provider.connector.ImageConnector;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@CucumberContextConfiguration
public class ImageProviderIntegrationTest extends StpIntegrationTest<Object> {

    @Autowired
    private ImageConnector imageConnector;

    @Override
    protected Object createDetails() {
        return null;
    }
}