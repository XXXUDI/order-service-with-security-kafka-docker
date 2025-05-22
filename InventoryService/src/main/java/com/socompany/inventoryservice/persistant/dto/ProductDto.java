package com.socompany.inventoryservice.persistant.dto;

import lombok.Data;

import java.util.UUID;

@Data
@Deprecated
public class ProductDto {

    private UUID id;
    private UUID ownerId;
    private String name;
    private String description;
    private Double price;
    private short quantity;

}
