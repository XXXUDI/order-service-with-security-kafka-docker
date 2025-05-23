package com.socompany.orderservice.persistant.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDto {

    private UUID uuid;
    private String deliverAddress;
    private String paymentMethod;
    private Inventory inventory;

}
