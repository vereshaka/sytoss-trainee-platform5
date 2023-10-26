package com.sytoss.users.connectors;

import com.sytoss.domain.bom.lessons.Discipline;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${lessons-url}", name = "disciplineConnector")
public interface DisciplineConnector {

    @GetMapping("/disciplines/byGroup/{groupId}")
    List<Discipline> getDisciplinesByGroupId(@PathVariable Long groupId);
}
