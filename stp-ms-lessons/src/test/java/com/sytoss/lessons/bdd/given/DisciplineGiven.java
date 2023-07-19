package com.sytoss.lessons.bdd.given;

import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.*;

public class DisciplineGiven extends AbstractGiven {

    @Given("^this teacher has \"(.*)\" discipline with id (.*) and following topics:")
    public void teacherHasDiscipline(String disciplineName, String disciplineId, DataTable topicsData) {
        Long teacherId = getTestExecutionContext().getDetails().getTeacherId();
        DisciplineDTO discipline = getDisciplineConnector().getByNameAndTeacherId(disciplineName, teacherId);
        if (discipline == null) {
            discipline = new DisciplineDTO();
            discipline.setName(disciplineName);
            discipline.setTeacherId(teacherId);
            discipline.setCreationDate(Timestamp.from(Instant.now()));
            discipline = getDisciplineConnector().save(discipline);
        }

        getTestExecutionContext().registerId(disciplineId, discipline.getId());

        List<String> topics = new ArrayList<>();
        List<TopicDTO> existTopics = getTopicConnector().findByDisciplineId(discipline.getId());

        for (int i = 1; i < topicsData.height(); i++) {
            String topicKey = topicsData.row(i).get(0).trim();
            String topicName = topicsData.row(i).get(1).trim();
            topics.add(topicName);
            TopicDTO result = existTopics.stream().filter(item -> item.getName().equalsIgnoreCase(topicName)).findFirst().orElse(null);
            if (result != null) {
                getTopicConnector().delete(result);
            }
            result = new TopicDTO();
            result.setName(topicName);
            result.setDiscipline(discipline);
            result = getTopicConnector().save(result);
            getTestExecutionContext().registerId(topicKey, result.getId());
        }
        deleteTopics(existTopics.stream().filter(item -> !topics.contains(item.getName().toLowerCase())).toList());
    }

    @Given("disciplines exist")
    public void disciplinesExist(DataTable dataTable) {
        List<Map<String, String>> rows = dataTable.asMaps();
        List<DisciplineDTO> disciplineDTOS = new ArrayList<>();
        for (Map<String, String> columns : rows) {
            String teacherId = columns.get("teacherId");
            String disciplineName = columns.get("discipline");
            String date = columns.get("creationDate");
            teacherId = teacherId != null ? teacherId : "1";
            Date creationDate = date != null ? toDate(date) : Timestamp.from(Instant.now());
            DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, Long.valueOf(teacherId));
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
            }
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacherId(Long.valueOf(teacherId));
            disciplineDTO.setCreationDate(Timestamp.from(creationDate.toInstant()));
            disciplineDTOS.add(disciplineDTO);
        }

        List<DisciplineDTO> disciplineDTOList = getDisciplineConnector().findAll();
        for (DisciplineDTO disciplineDTO : disciplineDTOList) {
            for (DisciplineDTO disciplineDtoFromTable : disciplineDTOS) {
                if (!(disciplineDTO.getName().equals(disciplineDtoFromTable.getName()))) {
                    getDisciplineConnector().deleteById(disciplineDTO.getId());
                }
                if (!(disciplineDTO.getTeacherId().equals(disciplineDtoFromTable.getTeacherId()))) {
                    getDisciplineConnector().deleteById(disciplineDTO.getId());
                }
            }
        }
        for (DisciplineDTO discipline : disciplineDTOS) {
            DisciplineDTO disciplineResult = getDisciplineConnector().getByNameAndTeacherId(discipline.getName(), discipline.getTeacherId());
            if (disciplineResult == null) {
                getDisciplineConnector().save(discipline);
            }
        }
    }

    @Given("^\"(.*)\" discipline exists for this teacher$")
    public void disciplineExistsForTeacher(String nameDiscipline) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(nameDiscipline, getTestExecutionContext().getDetails().getTeacherId());
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(nameDiscipline);
            disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
            disciplineDTO = getDisciplineConnector().save(disciplineDTO);
        }
        getTestExecutionContext().getDetails().setDisciplineId(disciplineDTO.getId());
    }

    @Given("^discipline \"(.*)\" doesn't exist$")
    public void disciplineNotExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        if (disciplineDTO != null) {
            getDisciplineConnector().delete(disciplineDTO);
        }
    }

    @Given("^\"(.*)\" discipline exists$")
    public void disciplineExist(String disciplineName) {
        DisciplineDTO disciplineDTO = getDisciplineConnector().getByNameAndTeacherId(disciplineName, getTestExecutionContext().getDetails().getTeacherId());
        if (disciplineDTO == null) {
            disciplineDTO = new DisciplineDTO();
            disciplineDTO.setName(disciplineName);
            disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
            disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
            disciplineDTO = getDisciplineConnector().save(disciplineDTO);
        }
        getTestExecutionContext().getDetails().setDisciplineId(disciplineDTO.getId());
    }

    @Given("^this discipline has icon with bytes \"([^\\\"]*)\"$")
    public void disciplineHasIcon(String iconBytes) {

        String[] numberStrings = iconBytes.split(", ");

        byte[] icon = new byte[numberStrings.length];

        for (int i = 0; i < numberStrings.length; i++) {
            icon[i] = Byte.parseByte(numberStrings[i]);
        }

        DisciplineDTO disciplineDTO = getDisciplineConnector().findById(getTestExecutionContext().getDetails().getDisciplineId()).orElse(null);
        disciplineDTO.setIcon(icon);
        getDisciplineConnector().save(disciplineDTO);
    }

    protected Date toDate(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            return format.parse(value);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    protected String format(Date value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(value);
    }
}
