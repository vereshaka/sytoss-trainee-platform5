package com.sytoss.producer.bdd;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sytoss.producer.bdd.common.TestProducerDetails;
import com.sytoss.producer.common.connectors.PersonalExamConnectorTest;
import com.sytoss.producer.connectors.CheckTaskConnector;
import com.sytoss.producer.connectors.ImageConnector;
import com.sytoss.producer.connectors.MetadataConnector;
import com.sytoss.producer.connectors.UserConnector;
import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Getter
@CucumberContextConfiguration
@ExtendWith(SpringExtension.class)
public class TestProducerIntegrationTest extends StpIntegrationTest<TestProducerDetails> {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper mapper;

    @LocalServerPort
    private int applicationPort;

    @MockBean
    @Autowired
    private CheckTaskConnector checkTaskConnector;

    @MockBean
    @Autowired
    private UserConnector userConnector;

    @Autowired
    private PersonalExamConnectorTest personalExamConnector;

    @Autowired
    @MockBean
    private ImageConnector imageConnector;

    @MockBean
    @Autowired
    private MetadataConnector metadataConnector;

    @Override
    protected TestProducerDetails createDetails() {
        return new TestProducerDetails();
    }
}
