package com.socompany.inventoryservice.persistant.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDto {

    UUID userId;
    private String username;
    private String password;
    private List<String> roles;
}
