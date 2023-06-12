package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TaskDTO;
import com.sytoss.lessons.dto.TaskDomainDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TaskDomainConvertor {

    private final DisciplineConvertor disciplineConvertor;

    private final TaskConvertor taskConvertor;

    public void fromDTO(TaskDomainDTO source, TaskDomain destination) {
        destination.setId(source.getId());
        if (source.getId() != null) {
            destination.setId(source.getId());
        }
        destination.setName(source.getName());
        destination.setScript(source.getScript());
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
        if (source.getTasks()!=null) {
            List<Task> taskList = new ArrayList<>();
            source.getTasks().forEach(taskDTO -> {
                Task task = new Task();
                taskConvertor.fromDTO(taskDTO, task);
                taskList.add(task);
            });
            destination.setTasks(taskList);
        }

    }

    public void toDTO(TaskDomain source, TaskDomainDTO destination) {
        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setScript(source.getScript());
        DisciplineDTO discipline = new DisciplineDTO();
        disciplineConvertor.toDTO(source.getDiscipline(), discipline);
        destination.setDiscipline(discipline);
        if (source.getTasks()!=null) {
            List<TaskDTO> taskDTOList = new ArrayList<>();
            source.getTasks().forEach(task -> {
                TaskDTO taskDTO = new TaskDTO();
                taskConvertor.toDTO(task, taskDTO);
                taskDTOList.add(taskDTO);
            });
            destination.setTasks(taskDTOList);
        }

    }
}
