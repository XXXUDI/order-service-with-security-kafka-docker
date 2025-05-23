package com.socompany.orderservice.mapper;

import com.socompany.orderservice.persistant.dto.OrderRequestDto;
import com.socompany.orderservice.persistant.dto.OrderResponseDto;
import com.socompany.orderservice.persistant.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "deliverAddress", target = "deliveryAddress")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "inventoryId", ignore = true)
    Order toEntity(OrderRequestDto dto);

    @Mapping(source = "deliveryAddress", target = "deliverAddress")
    @Mapping(source = "id", target = "uuid")
    @Mapping(target = "inventory", ignore = true)
    OrderResponseDto toDto(Order order);
}

