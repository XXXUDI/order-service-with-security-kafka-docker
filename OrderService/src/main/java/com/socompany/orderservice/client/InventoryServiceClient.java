package com.socompany.orderservice.client;

import com.socompany.orderservice.persistant.dto.Inventory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "inventory-service", path = "/api/inventory")
public interface InventoryServiceClient {

    @GetMapping("/user/{userId}")
    Inventory getInventoryByUserId(@PathVariable("userId") UUID userId);

    @PostMapping("/add")
    Inventory addProductToUserInventory(
            @RequestParam("userId") UUID userId,
            @RequestParam("productId") UUID productId
    );

    @DeleteMapping("/remove/inventory")
    void removeUserInventory(@RequestParam("userId") UUID userId);

    @DeleteMapping("/remove/product")
    void removeProductFromUserInventory(
            @RequestParam("userId") UUID userId,
            @RequestParam("productId") UUID productId
    );
}

