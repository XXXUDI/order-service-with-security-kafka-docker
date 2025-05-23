package com.socompany.orderservice.mapper;

import com.socompany.orderservice.persistant.dto.OrderDto;
import com.socompany.orderservice.persistant.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring", imports = {java.util.UUID.class, Instant.class})
public interface OrderMapper {


//    @Mapping(target = "id", expression = "java(order.getId() != null ? order.getId() : UUID.randomUUID())")
//    @Mapping(target = "version", constant = "0")
//    @Mapping(target = "createdAt", expression = "java(Instant.now())")
//    @Mapping(target = "updatedAt", expression = "java(Instant.now())")


    @Mapping(source = "id", target = "id")
    @Mapping(source = "inventoryId", target = "inventoryId")
    OrderDto toDto(Order order);

}
