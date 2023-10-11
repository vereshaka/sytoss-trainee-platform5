package com.sytoss.users.connectors;

import com.sytoss.domain.bom.users.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${lessons-url}", name = "examAssigneeConnector")
public interface ExamAssigneeConnector {

    @PostMapping(value = "/assignee/group/{groupId}")
    void createGroupExamsOnStudent(@PathVariable Long groupId, @RequestBody Student student);
}
