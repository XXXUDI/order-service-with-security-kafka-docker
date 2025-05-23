package com.socompany.inventoryservice.repository;

import com.socompany.inventoryservice.persistant.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    Optional<Inventory> findById(Integer id);

    Optional<Inventory> findByUserId(UUID userId);
}
