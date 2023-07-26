package com.sytoss.users.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.domain.bom.users.Student;
import com.sytoss.stp.test.StpUnitTest;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.connectors.UserConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.convertors.UserConverter;
import com.sytoss.users.dto.GroupDTO;
import com.sytoss.users.dto.StudentDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupServiceTest extends StpUnitTest {

    @Mock
    private GroupConnector groupConnector;

    @Spy
    private GroupConvertor groupConvertor = new GroupConvertor();

    @Spy
    private UserConverter userConverter = new UserConverter();


    @InjectMocks
    private GroupService groupService;

    @Test
    public void getByIdTest() {
        GroupDTO groupDTO = new GroupDTO();
        groupDTO.setId(1L);
        when(groupConnector.findById(any())).thenReturn(Optional.of(groupDTO));
        Group group = groupService.getById(1L);
        Assertions.assertEquals(1L, group.getId());
    }

    @Test
    public void getStudents() {
       GroupDTO groupDTO = new GroupDTO();
       groupDTO.setId(1L);
       groupDTO.setStudents(List.of(new StudentDTO(),new StudentDTO(), new StudentDTO()));
       when(groupConnector.findById(any())).thenReturn(Optional.of(groupDTO));
       List<Student> students = groupService.getStudents(1L);
       Assertions.assertEquals(groupDTO.getStudents().size(),students.size());
    }
}
