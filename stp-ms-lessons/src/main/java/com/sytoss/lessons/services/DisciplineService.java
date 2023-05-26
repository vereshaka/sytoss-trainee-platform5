package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DisciplineService {

    @Autowired
    private DisciplineConnector disciplineConnector;

    @Autowired
    private DisciplineConvertor disciplineConvertor;

    public Discipline getById(Long id) {
        DisciplineDTO disciplineDTO = disciplineConnector.getReferenceById(id);
        if (disciplineDTO != null) {
            Discipline discipline = new Discipline();
            disciplineConvertor.fromDTO(disciplineDTO, discipline);
            return discipline;
        } else {
            throw new DisciplineNotFoundException(id);
        }
    }
}
