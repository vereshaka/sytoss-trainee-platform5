package com.sytoss.lessons.connectors;

import com.sytoss.lessons.dto.AnalyticsDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestAnalyticsConnector extends AnalyticsConnector {

    List<AnalyticsDTO> findAllByDisciplineId(Long disciplineId);

}
