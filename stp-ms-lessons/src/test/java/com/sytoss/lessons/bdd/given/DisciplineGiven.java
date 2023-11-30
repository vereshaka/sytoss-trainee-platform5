package com.sytoss.lessons.bdd.given;

import com.sytoss.domain.bom.exceptions.business.notfound.DisciplineNotFoundException;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TopicDTO;
import com.sytoss.lessons.services.DisciplineService;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DisciplineGiven extends AbstractGiven {

    @Autowired
    private DisciplineService disciplineService;

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
        List<TopicDTO> existTopics = getTopicConnector().findByDisciplineIdOrderByName(discipline.getId());

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
        List<Long> shouldBeDeleted = new ArrayList<>();
        for (DisciplineDTO disciplineDTO : disciplineDTOList) {
            for (DisciplineDTO disciplineDtoFromTable : disciplineDTOS) {
                if (disciplineDTO.getName() != null) {
                    if (!(disciplineDTO.getName().equals(disciplineDtoFromTable.getName()))) {
                        if (!shouldBeDeleted.contains(disciplineDTO.getId())) {
                            shouldBeDeleted.add(disciplineDTO.getId());
                        }
                    }
                }
                if (disciplineDTO.getTeacherId() != null) {
                    if (!(disciplineDTO.getTeacherId().equals(disciplineDtoFromTable.getTeacherId()))) {
                        if (!shouldBeDeleted.contains(disciplineDTO.getId())) {
                            shouldBeDeleted.add(disciplineDTO.getId());
                        }
                    }
                }
            }
        }
        for (Long idToDelete : shouldBeDeleted) {
            disciplineService.delete(idToDelete);
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

    @Given("^discipline with specific id (.*) and specific teacher id (.*) exists")
    public void disciplineWithIdExists(Long disciplineId, Long teacherId) {
        try {
            disciplineService.delete(disciplineId);
        } catch (DisciplineNotFoundException e) {

        }
        try {
            Connection connection = getDataSource().getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM DISCIPLINE WHERE ID = " + disciplineId);
            statement.execute("INSERT INTO DISCIPLINE (ID, NAME, TEACHER_ID,CREATION_DATE) VALUES(" + disciplineId + ", 'discipline', " +
                    teacherId + ",'2023-08-28T07:03:39.655+00:00'" + ")");
            while (true) {
                ResultSet rs = statement.executeQuery("select DISCIPLINE_SEQ.nextVal from Dual");
                rs.next();
                int id = rs.getInt(1);
                if (id >= disciplineId) {
                    break;
                }
            }
            statement.close();
            connection.commit();
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM DISCIPLINE where ID = " + disciplineId);
            assertTrue(rs.next());
            rs.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        getEntityManager().clear();
        getTestExecutionContext().getDetails().setDisciplineId(disciplineId);
    }

    @Given("^discipline with id (.*) exists$")
    public void disciplineWithSpecificIdExists(String disciplineStringId) {
        String newDisciplineKey = disciplineStringId;
        if (getTestExecutionContext().replaceId(disciplineStringId) != null) {
            newDisciplineKey = getTestExecutionContext().replaceId(disciplineStringId).toString();
        }
        long disciplineId;
        DisciplineDTO disciplineDTO;
        if (!disciplineStringId.equals(newDisciplineKey)) {
            disciplineId = Long.parseLong(newDisciplineKey);
            disciplineDTO = getDisciplineConnector().findById(disciplineId).orElse(null);
            if (disciplineDTO == null) {
                disciplineDTO = new DisciplineDTO();
                disciplineDTO.setTeacherId(getTestExecutionContext().getDetails().getTeacherId());
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
            }
        }
    }

    @Given("^disciplines with specific id exist$")
    public void disciplinesWithSpecificIdExist(DataTable dataTable) {
        List<Map<String,String>> disciplines = dataTable.asMaps();
        for(Map<String,String> discipline : disciplines){
            String disciplineKey = getTestExecutionContext().replaceId(discipline.get("id")).toString();
            if(Objects.equals(disciplineKey, getTestExecutionContext().replaceId(discipline.get("id")).toString())){
                DisciplineDTO disciplineDTO = new DisciplineDTO();
                disciplineDTO.setName(discipline.get("name"));
                disciplineDTO.setTeacherId(1L);
                disciplineDTO.setCreationDate(Timestamp.from(Instant.now()));
                disciplineDTO = getDisciplineConnector().save(disciplineDTO);
                getTestExecutionContext().registerId(disciplineKey,disciplineDTO.getId());
            }
        }
    }
}
