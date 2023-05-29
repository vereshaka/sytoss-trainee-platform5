package com.sytoss.lessons.services;

import com.sytoss.domain.bom.exceptions.businessException.DisciplineNotFoundException;
import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.lessons.connectors.DisciplineConnector;
import com.sytoss.lessons.convertors.DisciplineConvertor;
import com.sytoss.lessons.dto.DisciplineDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class DisciplineService {

    private final DisciplineConnector disciplineConnector;

    private final DisciplineConvertor disciplineConvertor;

    public Discipline getById(Long id) {
        DisciplineDTO disciplineDTO = disciplineConnector.findById(id).orElseThrow(() -> new DisciplineNotFoundException(id));
        Discipline discipline = new Discipline();
        disciplineConvertor.fromDTO(disciplineDTO, discipline);
        return discipline;
    }
}
