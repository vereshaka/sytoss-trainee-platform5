package com.sytoss.lessons.convertors;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Teacher;
import com.sytoss.lessons.AbstractJunitTest;
import com.sytoss.lessons.dto.DisciplineDTO;
import com.sytoss.lessons.dto.TeacherDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisciplineConvertorTest extends AbstractJunitTest {

    @Spy
    private DisciplineConvertor disciplineConvertor = new DisciplineConvertor(new TeacherConvertor());

    @Test
    public void fromDTODisciplineConvertorTest() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(62L);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineDTO.setId(93L);
        disciplineDTO.setName("SQL");
        disciplineDTO.setTeacher(teacherDTO);
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(disciplineDTO, discipline);
        assertEquals(disciplineDTO.getId(), discipline.getId());
        assertEquals(disciplineDTO.getName(), discipline.getName());
        assertEquals(disciplineDTO.getTeacher().getId(), discipline.getTeacher().getId());
    }

    @Test
    public void toDTODisciplineConvertorTest() {
        Teacher teacher = new Teacher();
        teacher.setId(62L);
        Discipline discipline = new Discipline();
        discipline.setId(93L);
        discipline.setName("SQL");
        discipline.setTeacher(teacher);
        DisciplineDTO disciplineDTO = new DisciplineDTO();
        disciplineConvertor.toDTO(discipline, disciplineDTO);
        assertEquals(discipline.getId(), disciplineDTO.getId());
        assertEquals(discipline.getName(), disciplineDTO.getName());
        assertEquals(discipline.getTeacher().getId(), disciplineDTO.getTeacher().getId());
    }
}
