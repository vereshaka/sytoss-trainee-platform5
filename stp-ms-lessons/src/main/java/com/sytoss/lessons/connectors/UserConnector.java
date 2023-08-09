package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.users.AbstractUser;
import com.sytoss.domain.bom.users.Teacher;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(url = "${users-url}", name = "userConnector")
public interface UserConnector {

    @GetMapping("user/me")
    Object getMyProfile();

    @GetMapping("user/me/groupsId")
    List<Long> findMyGroupId();
}
