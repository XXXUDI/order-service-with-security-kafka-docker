package com.socompany.orderservice.persistant.dto;

import lombok.Data;

@Data
public class OrderRequestDto {
    private String deliverAddress;
    private String paymentMethod;
}
