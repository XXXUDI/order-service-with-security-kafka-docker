package com.socompany.inventoryservice.controller;

import com.socompany.inventoryservice.persistant.dto.UserDto;
import com.socompany.inventoryservice.persistant.entity.Inventory;
import com.socompany.inventoryservice.service.InventoryService;
import com.socompany.inventoryservice.service.UserServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/inventory")
public class InventoryController {

    private final UserServiceClient userServiceClient;
    private final InventoryService inventoryService;

    public InventoryController(UserServiceClient userServiceClient,
                               InventoryService inventoryService) {
        this.userServiceClient = userServiceClient;
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public ResponseEntity<Inventory> getUserInventoryById(Authentication authentication) {
        log.info("Received request to get inventory by user: {}", authentication.getName());
        UserDto user = userServiceClient.getUserByUsername(authentication.getName());
        return ResponseEntity.ok(inventoryService.getInventoryByUserId(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    // This function will be used to add one Product per request to user Inventory
    @PostMapping("/add")
    public ResponseEntity<Inventory> addProductToUserInventory(Authentication authentication,
                                                UUID productId) {
        log.info("Received request to add product to inv. by user: {}", authentication.getName());
        UserDto user = userServiceClient.getUserByUsername(authentication.getName());
        inventoryService.getInventoryByUserId(user.getUserId()).ifPresent(inventory -> {
            inventory.getProductIds().add(productId);
            inventory.setUserId(user.getUserId());
            inventoryService.saveOrUpdateInventory(inventory);
        });

        return ResponseEntity.ok(inventoryService.getInventoryByUserId(user.getUserId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @DeleteMapping("/remove/inventory")
    public ResponseEntity<Inventory> removeUserInventory(Authentication authentication) {
        log.info("Received request to delete inventory by user: {}", authentication.getName());
        UserDto user = userServiceClient.getUserByUsername(authentication.getName());

        if(!inventoryService.getInventoryByUserId(user.getUserId()).isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/remove/product")
    public ResponseEntity<Inventory> removeProductFromUserInventory(Authentication authentication, UUID productId) {
        log.info("Received request to delete product from inventory by user: {}", authentication.getName());
        UserDto user = userServiceClient.getUserByUsername(authentication.getName());
        inventoryService.getInventoryByUserId(user.getUserId()).ifPresent(inventory -> {
            inventory.getProductIds().remove(productId);
            inventory.setUserId(user.getUserId());
            inventoryService.saveOrUpdateInventory(inventory);
        });

        return ResponseEntity.status(HttpStatus.OK).build();
    }



}
