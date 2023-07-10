package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.business.EtalonIsNotValidException;
import com.sytoss.domain.bom.exceptions.business.TaskDomainAlreadyExist;
import com.sytoss.domain.bom.exceptions.business.TaskDomainCouldNotCreateImageException;
import com.sytoss.domain.bom.exceptions.business.TaskDomainIsUsed;
import com.sytoss.domain.bom.exceptions.business.notfound.TaskDomainNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.lessons.Task;
import com.sytoss.domain.bom.lessons.TaskDomain;
import com.sytoss.domain.bom.personalexam.CheckRequestParameters;
import com.sytoss.domain.bom.personalexam.IsCheckEtalon;
import com.sytoss.lessons.bom.TaskDomainModel;
import com.sytoss.lessons.connectors.CheckTaskConnector;
import com.sytoss.lessons.connectors.PersonalExamConnector;
import com.sytoss.lessons.connectors.TaskDomainConnector;
import com.sytoss.lessons.convertors.PumlConvertor;
import com.sytoss.lessons.convertors.TaskDomainConvertor;
import com.sytoss.lessons.dto.TaskDomainDTO;
import com.sytoss.lessons.enums.ConvertToPumlParameters;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class TaskDomainService {

    private final TaskDomainConnector taskDomainConnector;

    private final TaskDomainConvertor taskDomainConvertor;

    private final DisciplineService disciplineService;

    private final PersonalExamConnector personalExamConnector;

    private final CheckTaskConnector checkTaskConnector;

    private final TaskService taskService;

    private final PumlConvertor pumlConvertor;

    public TaskDomain create(Long disciplineId, TaskDomain taskDomain) {
        Discipline discipline = disciplineService.getById(disciplineId);
        TaskDomainDTO oldTaskDomainDTO = taskDomainConnector.getByNameAndDisciplineId(taskDomain.getName(), disciplineId);
        if (oldTaskDomainDTO == null) {
            taskDomain.setDiscipline(discipline);
            TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
            taskDomainConvertor.toDTO(taskDomain, taskDomainDTO);
            taskDomainDTO = taskDomainConnector.save(taskDomainDTO);
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            return taskDomain;
        } else {
            throw new TaskDomainAlreadyExist(taskDomain.getName());
        }
    }

    public TaskDomain update(Long taskDomainId, TaskDomain taskDomain) {
        TaskDomain oldTaskDomain = getById(taskDomainId);
        boolean isUsed = personalExamConnector.taskDomainIsUsed(oldTaskDomain.getId());
        if (isUsed) {
            throw new TaskDomainIsUsed(oldTaskDomain.getName());
        }
        List<Task> tasks = taskService.findByDomainId(oldTaskDomain.getId());
        for (Task task : tasks) {
            CheckRequestParameters input = new CheckRequestParameters();
            input.setRequest(task.getEtalonAnswer());
            input.setScript(taskDomain.getScript());
            IsCheckEtalon isCheckEtalon = checkTaskConnector.checkEtalon(input);
            if (!isCheckEtalon.isChecked()) {
                throw new EtalonIsNotValidException("etalon isn't correct for task with question \"" + task.getQuestion()
                        + "\"\nWith message exception\n" + isCheckEtalon.getException());
            }
        }
        oldTaskDomain.setName(taskDomain.getName());
        oldTaskDomain.setScript(taskDomain.getScript());
        oldTaskDomain.setDiscipline(taskDomain.getDiscipline());
        TaskDomainDTO taskDomainDTO = new TaskDomainDTO();
        taskDomainConvertor.toDTO(oldTaskDomain, taskDomainDTO);
        taskDomainDTO = taskDomainConnector.save(taskDomainDTO);
        taskDomainConvertor.fromDTO(taskDomainDTO, oldTaskDomain);
        return oldTaskDomain;
    }

    public TaskDomain getById(Long taskDomainId) {
        try {
            TaskDomainDTO taskDomainDTO = taskDomainConnector.getReferenceById(taskDomainId);
            TaskDomain taskDomain = new TaskDomain();
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            return taskDomain;
        } catch (EntityNotFoundException exception) {
            throw new TaskDomainNotFoundException(taskDomainId);
        }
    }

    public List<TaskDomain> findByDiscipline(Long disciplineId) {
        List<TaskDomainDTO> taskDomainDTOList = taskDomainConnector.findByDisciplineId(disciplineId);
        List<TaskDomain> result = new ArrayList<>();
        for (TaskDomainDTO taskDomainDTO : taskDomainDTOList) {
            TaskDomain taskDomain = new TaskDomain();
            taskDomainConvertor.fromDTO(taskDomainDTO, taskDomain);
            result.add(taskDomain);
        }
        return result;
    }

    public byte[] generatePngFromPuml(String puml, ConvertToPumlParameters convertToPumlParameters) {
        String newPuml=puml;
        if(convertToPumlParameters.equals(ConvertToPumlParameters.DB)){
            List<String> entities = pumlConvertor.getEntities(puml);
            newPuml = String.join(StringUtils.LF+StringUtils.LF,entities);
        }else if(convertToPumlParameters.equals(ConvertToPumlParameters.DATA)){
            List<String> objects = pumlConvertor.getObjects(puml);
            newPuml = String.join(StringUtils.LF+StringUtils.LF,objects);
        }

        String pumlConvertedScript = pumlConvertor.addLinks(newPuml,puml,convertToPumlParameters);
        ByteArrayOutputStream png = new ByteArrayOutputStream();
        try {
            SourceStringReader reader = new SourceStringReader(pumlConvertedScript);
            String result = reader.outputImage(png).getDescription();

            File imageFile = File.createTempFile("img", ".png");
            ByteArrayInputStream bis = new ByteArrayInputStream(png.toByteArray());
            BufferedImage bufferedImage = ImageIO.read(bis);
            ImageIO.write(bufferedImage, "png", imageFile);

            if (!result.isEmpty()) {
                return png.toByteArray();
            }
        } catch (Exception e) {
            throw new TaskDomainCouldNotCreateImageException();
        }
        return null;
    }

    public TaskDomainModel getCountOfTasks(Long taskDomainId) {
        List<Task> tasks = taskService.findByDomainId(taskDomainId);
        TaskDomainModel taskDomainModel = new TaskDomainModel();
        taskDomainModel.setCountOfTasks(tasks.size());
        return taskDomainModel;
    }
}