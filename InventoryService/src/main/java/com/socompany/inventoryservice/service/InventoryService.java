package com.socompany.inventoryservice.service;

import com.socompany.inventoryservice.persistant.entity.Inventory;
import com.socompany.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Deprecated
    public Inventory saveOrUpdateInventory(Inventory inventory) {
        log.info("Saving or updating inventory: {}", inventory);
        return inventoryRepository.save(inventory);
    }

    @Deprecated
    public void deleteInventory(Inventory inventory) {
        log.info("Deleting inventory: {}", inventory);
        inventoryRepository.delete(inventory);
    }

    public boolean deleteInventoryByUserId(UUID uuid) {
        return inventoryRepository.findByUserId(uuid)
                .map(inventory -> {
                    inventoryRepository.delete(inventory);
                    return true;
                })
                .orElse(false);
    }

    public Optional<Inventory> getInventoryByUserId(UUID uuid) {
        log.info("Getting inventory by user id: {}", uuid);
        return inventoryRepository.findByUserId(uuid);
    }


}
