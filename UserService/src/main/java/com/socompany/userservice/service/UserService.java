package com.socompany.userservice.service;

import com.socompany.userservice.dto.UserDto;
import com.socompany.userservice.mapper.UserMapper;
import com.socompany.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

        if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
            userDto.setRoles(Collections.singletonList("USER"));
        }

        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userMapper.toDto(userRepository.save(userMapper.toEntity(userDto)));
    }

    public Optional<UserDto> updateUserByUsername(String username, UserDto userDto,
                                                  String authenticatedUser, List<String> userRoles) throws AccessDeniedException {
        log.info("Trying to find user, with username: {}", username);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        log.info("User found: {}", user);
        boolean isAdmin = userRoles.stream().anyMatch(role -> role.equals("ROLE_ADMIN"));
        log.info("User {} is admin: {}", authenticatedUser, isAdmin);
        boolean isSelf = authenticatedUser.equals(username);

        if (!isAdmin && !isSelf) {
            log.error("User {} is not admin and is not self.", authenticatedUser);
            throw new AccessDeniedException("Access denied");
        }

        if (userDto.getPassword() != null && !userDto.getPassword().isBlank()) {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if (userDto.getUsername() != null && !userDto.getUsername().isBlank()) {
            user.setUsername(userDto.getUsername());
        }

        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            log.info("User {} is trying to update roles.", authenticatedUser);
            if (!isAdmin) {
                log.error("User {} is trying to update roles, but is not admin.", authenticatedUser);
                throw new AccessDeniedException("Access denied");
            }
            user.setRoles(userDto.getRoles());
        }
        user.setLastModifiedDate(Instant.now());

        log.info("User updated, trying to save user to db:");
        return Optional.of(userMapper.toDto(userRepository.save(user)));
    }

    public boolean deleteUserByUsername(String username, String authenticatedUser,
                                        List<String> userRoles) throws AccessDeniedException {
        log.info("Deleting user: {}", username);
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User {} is trying to delete user, but is not found.", username);
                    return new IllegalArgumentException("User not found");
                });

        boolean isAdmin = userRoles.stream().anyMatch(role -> role.equals("ROLE_ADMIN"));
        log.info("User {} is admin: {}", authenticatedUser, isAdmin);
        if (!isAdmin) {
            log.error("User {} is trying to delete user, but is not admin.", authenticatedUser);
            throw new AccessDeniedException("Access denied");
        }

        userRepository.delete(user);
        userRepository.flush();
        log.info("User deleted.");
        return true;

    }

}
