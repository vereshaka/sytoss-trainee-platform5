package com.sytoss.lessons.bdd.common;

import com.sytoss.lessons.bdd.LessonsIntegrationTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class DataTableConfigurator extends LessonsIntegrationTest {

    @DataTableType
    public TopicDTO mapGroupDTO(Map<String, String> row) {
        TopicDTO result = new TopicDTO();
        result.setName(row.get("topic"));
        result.setDiscipline(new DisciplineDTO());
        result.getDiscipline().setName(row.get("discipline"));
        return result;
    }
}
