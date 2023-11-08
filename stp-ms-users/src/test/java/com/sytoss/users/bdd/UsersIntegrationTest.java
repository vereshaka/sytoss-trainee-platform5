package com.sytoss.users.bdd;

import com.sytoss.stp.test.cucumber.StpIntegrationTest;
import com.sytoss.users.bdd.common.UsersDetails;
import com.sytoss.users.connectors.ExamAssigneeConnector;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.connectors.ImageProviderConnector;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.convertors.UserConverter;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Getter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@CucumberContextConfiguration
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@Getter
public class UsersIntegrationTest extends StpIntegrationTest<UsersDetails> {

    @Autowired
    private UserConnector userConnector;

    @Autowired
    private GroupConnector groupConnector;

    @Autowired
    private UserConverter userConverter;

    @Autowired
    private GroupConvertor groupConvertor;

    @MockBean
    private ImageProviderConnector imageProviderConnector;

    @MockBean
    private ExamAssigneeConnector examAssigneeConnector;

    @Override
    protected UsersDetails createDetails() {
        return new UsersDetails();
    }
}
