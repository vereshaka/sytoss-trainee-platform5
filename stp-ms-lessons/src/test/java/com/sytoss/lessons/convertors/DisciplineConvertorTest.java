package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.stp.test.StpUnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisciplineConvertorTest extends StpUnitTest {

    @Spy
    private DisciplineConvertor disciplineConvertor = new DisciplineConvertor();

    @Test
    public void fromDTODisciplineConvertorTest() {
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(93L);
        disciplineDTO.setName("SQL");
        disciplineDTO.setTeacherId(1L);
        disciplineDTO.setShortDescription("test short description");
        disciplineDTO.setFullDescription("test full description");
        disciplineDTO.setDuration(1.5);
        byte[] iconBytes = {0x01, 0x02, 0x03};
        disciplineDTO.setIcon(iconBytes);
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(disciplineDTO, discipline);
        assertEquals(disciplineDTO.getId(), discipline.getId());
        assertEquals(disciplineDTO.getName(), discipline.getName());
        assertEquals(disciplineDTO.getTeacherId(), discipline.getTeacher().getId());
        assertEquals(disciplineDTO.getShortDescription(), discipline.getShortDescription());
        assertEquals(disciplineDTO.getFullDescription(), discipline.getFullDescription());
        assertEquals(disciplineDTO.getDuration(), discipline.getDuration());
        assertEquals(disciplineDTO.getIcon(), discipline.getIcon());
    }

    @Test
    public void toDTODisciplineConvertorTest() {
        Teacher teacher = new Teacher();
        teacher.setId(62L);
        Discipline discipline = new Discipline();
        discipline.setId(93L);
        discipline.setName("SQL");
        discipline.setTeacher(teacher);
        discipline.setShortDescription("test short description");
        discipline.setFullDescription("test full description");
        discipline.setDuration(1.5);
        byte[] iconBytes = {0x01, 0x02, 0x03};
        discipline.setIcon(iconBytes);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineConvertor.toDTO(discipline, disciplineDTO);
        assertEquals(discipline.getId(), disciplineDTO.getId());
        assertEquals(discipline.getName(), disciplineDTO.getName());
        assertEquals(discipline.getTeacher().getId(), disciplineDTO.getTeacherId());
        assertEquals(discipline.getShortDescription(), disciplineDTO.getShortDescription());
        assertEquals(discipline.getFullDescription(), disciplineDTO.getFullDescription());
        assertEquals(discipline.getDuration(), disciplineDTO.getDuration());
        assertEquals(discipline.getIcon(), disciplineDTO.getIcon());
    }
}
