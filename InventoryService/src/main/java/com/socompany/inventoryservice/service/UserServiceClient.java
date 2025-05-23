package com.socompany.inventoryservice.service;

import com.socompany.inventoryservice.persistant.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("api/users/{username}")
    public UserDto getUserByUsername(@PathVariable("username") String username);
}
