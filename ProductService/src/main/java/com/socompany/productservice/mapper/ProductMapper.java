package com.socompany.productservice.mapper;

import com.socompany.productservice.persistant.dto.ProductDto;
import com.socompany.productservice.persistant.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {java.util.UUID.class, java.time.Instant.class})
public interface ProductMapper {

    @Mapping(target = "id", expression = "java(productDto.getId() != null ? productDto.getId() : UUID.randomUUID())")
    @Mapping(target = "createdDate", ignore = true) // Handled by @CreatedDate
    @Mapping(target = "lastModifiedDate", ignore = true) // Handled by @LastModifiedDate
    @Mapping(target = "version", ignore = true) // Handled by Hibernate
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "ownerId", target = "ownerId")
    @Mapping(source = "quantity", target = "quantity")
    Product toEntity(ProductDto productDto);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "ownerId", target = "ownerId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "quantity", target = "quantity")
    ProductDto toDto(Product product);
}

