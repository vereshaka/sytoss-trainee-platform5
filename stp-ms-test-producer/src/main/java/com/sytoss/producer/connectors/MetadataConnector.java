package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.lessons.Topic;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Headers({"Content-type: application/json", "Accept: application/json"})
@FeignClient(name = "metadataClient", url = "${lessons-url}")
public interface MetadataConnector {

    @GetMapping("/api/discipline/{disciplineId}")
    Discipline getDiscipline(@PathVariable("disciplineId") Long id);

    @GetMapping("/api/topic/{topicId}")
    Topic getTopic(@PathVariable("topicId") Long id);

    @GetMapping("/api/topic/{topicId}/tasks")
    List<Task> getTasksForTopic(@PathVariable("topicId") Long id);

    @GetMapping("/api/taskDomain/{taskDomainId}")
    TaskDomain getTaskDomain(@PathVariable("taskDomainId") Long id);

    @GetMapping("/api/task/{taskId}")
    Task getTaskById(@PathVariable("taskId") Long id);
}
