package com.socompany.productservice.mapper;

import com.socompany.productservice.persistant.dto.ProductDto;
import com.socompany.productservice.persistant.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {java.util.UUID.class, java.time.Instant.class})
public interface ProductMapper {

    @Mapping(target = "id", expression = "java(UUID.randomUUID())")
    @Mapping(target = "createdDate", expression = "java(Instant.now())")
    @Mapping(target = "lastModifiedDate", expression = "java(Instant.now())")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "version", constant = "0L")
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

