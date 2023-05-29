package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.notFound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.AbstractApplicationTest;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.dto.DisciplineDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class DisciplineServiceTest extends AbstractApplicationTest {

    @MockBean
    private DisciplineConnector disciplineConnector;

    @InjectMocks
    @Autowired
    private DisciplineService disciplineService;

    @Test
    public void getDisciplineById() {
        DisciplineDTO input = new DisciplineDTO();
        input.setId(1L);
        input.setName("SQL");
        when(disciplineConnector.getReferenceById(any())).thenReturn(input);
        Discipline result = disciplineService.getById(1L);
        assertEquals(input.getId(), result.getId());
        assertEquals(input.getName(), result.getName());
    }

    @Test
    public void shouldRaiseExceptionWhenDisciplineNotExist() {
        when(disciplineConnector.findById(1L)).thenThrow(new DisciplineNotFoundException(1L));
        assertThrows(DisciplineNotFoundException.class, () -> disciplineService.getById(1L));
    }
}