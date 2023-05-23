package com.sytoss.lessons.services;

import com.sytoss.domain.bom.lessons.Discipline;
import com.sytoss.domain.bom.users.Group;
import com.sytoss.lessons.AbstractSTPLessonsApplicationTest;
import com.sytoss.lessons.connectors.GroupConnector;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupServiceTest extends AbstractSTPLessonsApplicationTest {

    @InjectMocks
    private GroupService groupService;

    @MockBean
    private GroupConnector groupConnector;

    @Test
    public void findGroups() {
        Group group = new Group();
        group.setId("11");
        group.setName("Test");
        Discipline discipline = new Discipline();
        discipline.setName("SQL");
        group.setDiscipline(discipline);
        List<Group> input = new ArrayList<>();
        input.add(group);
        //when(groupConnector.findByDiscipline_Id(any())).then(input);


    }
}
