package com.socompany.orderservice.event;

import com.socompany.orderservice.persistant.dto.Inventory;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
public class OrderCreatedEvent implements Serializable {
    private UUID uuid;
    private String deliverAddress;
    private String paymentMethod;
    private UUID inventoryId;

}
