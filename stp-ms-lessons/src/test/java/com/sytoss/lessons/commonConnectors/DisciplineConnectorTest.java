package com.sytoss.lessons.commonConnectors;

import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.dto.DisciplineDTO;

public interface DisciplineConnectorTest extends DisciplineConnector {

    DisciplineDTO getByName(String disciplineName);
}
