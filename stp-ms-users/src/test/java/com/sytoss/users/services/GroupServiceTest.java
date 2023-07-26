package com.sytoss.users.services;

import com.sytoss.domain.bom.users.Group;
import com.sytoss.stp.test.StpUnitTest;
import com.sytoss.users.connectors.GroupConnector;
import com.sytoss.users.convertors.GroupConvertor;
import com.sytoss.users.dto.GroupDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class GroupServiceTest extends StpUnitTest {

    @Mock
    private GroupConnector groupConnector;

    @Spy
    private GroupConvertor groupConvertor = new GroupConvertor();

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
}
