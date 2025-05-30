package com.socompany.orderservice.service;

import com.socompany.orderservice.client.InventoryServiceClient;
import com.socompany.orderservice.mapper.OrderMapper;
import com.socompany.orderservice.persistant.dto.Inventory;
import com.socompany.orderservice.persistant.dto.OrderRequestDto;
import com.socompany.orderservice.persistant.dto.OrderResponseDto;
import com.socompany.orderservice.persistant.enums.RetryableTaskType;
import com.socompany.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RetryableTaskService retryableTaskService;
    private final InventoryServiceClient inventoryServiceClient;

    public OrderResponseDto createOrder(UUID userId, OrderRequestDto orderRequestDto) {
        log.info("Received request to create order for user: {}", userId);
        var order = orderMapper.toEntity(orderRequestDto);
        Inventory inventory = inventoryServiceClient.getInventoryByUserId(userId);
        order.setInventoryId(inventory.getInventoryId());
        log.info("Order created: {}", order);
        retryableTaskService.createRetryableTask(order, RetryableTaskType.SEND_CREATE_DELIVERY_REQUEST);
        log.info("Initialized retryable task");
        return orderMapper.toDto(orderRepository.save(order));
    }
}
