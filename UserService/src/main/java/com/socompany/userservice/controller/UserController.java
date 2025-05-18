package com.socompany.userservice.controller;

import com.socompany.userservice.service.UserService;
import com.socompany.userservice.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {


    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUserByUsername(@PathVariable String username) {
        log.info("Received request to get user: {}", username);
        UserDto user = userService.getUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Received request to save user: {}", userDto);
        return ResponseEntity.ok(userService.saveUser(userDto));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,
                              @PathVariable String username,
                              Authentication authentication) throws AccessDeniedException {

        log.info("Received request to update user: {}", userDto);

        String authenticatedUser = authentication.getName();
        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var user =  userService.updateUserByUsername(username, userDto, authenticatedUser, userRoles)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(user);

    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteUser(@RequestParam String username,
                                             Authentication authentication) throws AccessDeniedException {
        log.info("Received update request for username: {} by user: {}, authorities: {}", username, authentication.getName(), authentication.getAuthorities());
        String authenticatedUser = authentication.getName();
        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        boolean isDeleted = userService.deleteUserByUsername(username, authenticatedUser, userRoles);
        return ResponseEntity.ok(isDeleted);
    }



}
