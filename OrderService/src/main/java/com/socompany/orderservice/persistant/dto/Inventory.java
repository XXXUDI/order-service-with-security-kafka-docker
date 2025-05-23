package com.socompany.orderservice.persistant.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Inventory {
    private UUID userId;
    private UUID inventoryId;
    private List<UUID> productIds;
}
