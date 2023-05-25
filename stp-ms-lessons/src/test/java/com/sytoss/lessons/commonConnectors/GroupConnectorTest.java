package com.sytoss.lessons.commonConnectors;

import com.sytoss.lessons.connectors.GroupConnector;
import com.sytoss.lessons.dto.GroupDTO;

public interface GroupConnectorTest extends GroupConnector {

    GroupDTO getByNameAndDisciplineId(String groupName, Long disciplineId);
}
