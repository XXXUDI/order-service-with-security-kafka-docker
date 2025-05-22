package com.socompany.inventoryservice.persistant.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Inventory {
    @Id @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer inventoryId;

    @Column(unique = true, nullable = false)
    private UUID userId; // user id

    @ElementCollection
    private List<UUID> productIds; // List of user's products
}
