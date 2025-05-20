package com.socompany.productservice.persistant.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class Product extends BaseEntity {

    private UUID ownerId;
    @Column(unique = true)
    private String name;
    private String description;
    private Double price;
    private short quantity;

}
