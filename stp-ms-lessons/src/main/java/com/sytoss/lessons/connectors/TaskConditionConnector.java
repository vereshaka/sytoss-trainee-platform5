package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.lessons.ConditionType;
import com.sytoss.lessons.dto.TaskConditionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskConditionConnector extends JpaRepository<TaskConditionDTO, Long> {

    TaskConditionDTO getByNameAndTypeAndTaskId(String taskConditionName, ConditionType type, Long taskId);
}
