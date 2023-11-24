package com.sytoss.producer.connectors;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "${users-url}", name = "userConnector")
public interface UserConnector {

    @GetMapping("user/me")
    Object getMyProfile();

    @GetMapping("user/me/groupsId")
    List<Long> findMyGroupId();

    @GetMapping("group/{groupId}/students")
    List<Student> getStudentOfGroup(@PathVariable("groupId") Long groupId);

    @GetMapping("user/{uid}")
    AbstractUser getByUid(@PathVariable("uid") String uid);
}
