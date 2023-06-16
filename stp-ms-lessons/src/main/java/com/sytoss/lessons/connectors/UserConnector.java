package com.sytoss.lessons.connectors;

import com.sytoss.domain.bom.users.Teacher;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "http://localhost:9102/api/user", name = "userConnector")
public interface UserConnector {

    @GetMapping("/me")
    Teacher getMyProfile();
}
