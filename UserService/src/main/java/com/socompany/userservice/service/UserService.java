package com.socompany.userservice.service;

import com.socompany.userservice.dto.UserDto;
import com.socompany.userservice.mapper.UserMapper;
import com.socompany.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public Optional<UserDto> getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return userRepository.findByUsername(username).map(userMapper::toDto);
    }

    public UserDto saveUser(UserDto userDto) {
        log.info("Saving user: {}", userDto);

        if(userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            userDto.setRoles(Collections.singletonList("USER"));
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

}
