package com.socompany.securityservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private String username;
    private String password;
    private List<String> roles;
}
