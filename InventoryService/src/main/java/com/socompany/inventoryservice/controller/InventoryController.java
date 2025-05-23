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
// ----------------------------------------------------------------------------------------------------- //

    @GetMapping("/user/{userId}")
    public ResponseEntity<Inventory> getUserInventoryById(@PathVariable UUID userId) {
        return ResponseEntity.ok(inventoryService.getInventoryByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @PostMapping("/add")
    public ResponseEntity<Inventory> addProductToUserInventory(@RequestParam UUID userId,
                                                               @RequestParam UUID productId) {
        inventoryService.getInventoryByUserId(userId).ifPresent(inventory -> {
            inventory.getProductIds().add(productId);
            inventory.setUserId(userId);
            inventoryService.saveOrUpdateInventory(inventory);
        });

        return ResponseEntity.ok(inventoryService.getInventoryByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")));
    }

    @DeleteMapping("/remove/inventory")
    public ResponseEntity<Void> removeUserInventory(@RequestParam UUID userId) {
        if(!inventoryService.getInventoryByUserId(userId).isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/remove/product")
    public ResponseEntity<Void> removeProductFromUserInventory(@RequestParam UUID userId,
                                                               @RequestParam UUID productId) {

        inventoryService.getInventoryByUserId(userId).ifPresent(inventory -> {
            inventory.getProductIds().remove(productId);
            inventory.setUserId(userId);
            inventoryService.saveOrUpdateInventory(inventory);
        });

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}
