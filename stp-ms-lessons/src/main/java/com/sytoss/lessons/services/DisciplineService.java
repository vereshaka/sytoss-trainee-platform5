package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.notFound.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DisciplineService {

    @Autowired
    private DisciplineConnector disciplineConnector;

    @Autowired
    private DisciplineConvertor disciplineConvertor;

    public Discipline getById(Long disciplineId) {
        DisciplineDTO discipline = disciplineConnector.getReferenceById(disciplineId);;
        if (discipline.getId() == null) {
            throw new DisciplineNotFoundException(disciplineId);
        } else {
            Discipline result = new Discipline();
            disciplineConvertor.fromDTO(discipline, result);
            return result;
        }
    }
}
