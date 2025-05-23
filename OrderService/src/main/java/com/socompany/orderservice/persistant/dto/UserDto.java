package com.socompany.orderservice.persistant.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String username;
    private String password;
    private List<String> roles;
}
