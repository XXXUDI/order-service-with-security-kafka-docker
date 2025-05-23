package com.socompany.orderservice.persistant.entity;

import com.socompany.orderservice.persistant.dto.Inventory;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Order extends BaseEntity {
    private String deliveryAddress; // For example
    private String paymentMethod;

    private Inventory inventory; // Contain
}

