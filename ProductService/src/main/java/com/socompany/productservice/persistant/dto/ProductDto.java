package com.socompany.productservice.persistant.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductDto {
    private UUID id;
    private UUID ownerId;
    @NotNull @Size(min = 3, max = 20)
    private String name;
    private String description;
    @NotNull
    private Double price;
    @NotNull
    private short quantity;
}
