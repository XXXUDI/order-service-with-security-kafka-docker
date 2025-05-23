package com.socompany.orderservice.persistant.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderResponseDto {

    private UUID uuid;
    private String deliverAddress;
    private String paymentMethod;
    private Inventory inventory;

}
