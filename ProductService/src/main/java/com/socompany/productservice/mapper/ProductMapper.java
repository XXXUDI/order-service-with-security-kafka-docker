package com.socompany.productservice.mapper;

import com.socompany.productservice.persistant.dto.ProductDto;
import com.socompany.productservice.persistant.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = {java.util.UUID.class, java.time.Instant.class})
public interface ProductMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "lastModifiedDate", expression = "java(java.time.Instant.now())")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "description", target = "description")
    @Mapping(target = "version", constant = "0")
    @Mapping(source = "ownerId", target = "ownerId")
    Product toEntity(ProductDto productDto);

    @Mapping(source = "id", target = "uuid")
    @Mapping(source = "ownerId", target = "ownerId")
    ProductDto toDto(Product product);

}
