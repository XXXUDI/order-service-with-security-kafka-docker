package com.socompany.securityservice.service;

import com.socompany.securityservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "localhost:8080/api/users")
public interface UserServiceClient {

    @GetMapping("/{username}")
    UserDto getUserByUsername(@PathVariable("username") String username);
}
