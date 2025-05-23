package com.socompany.orderservice.service;

import com.socompany.orderservice.client.InventoryServiceClient;
import com.socompany.orderservice.mapper.OrderMapper;
import com.socompany.orderservice.persistant.dto.Inventory;
import com.socompany.orderservice.persistant.dto.OrderDto;
import com.socompany.orderservice.persistant.entity.Order;
import com.socompany.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RetryableTaskService retryableTaskService;
    private final InventoryServiceClient inventoryServiceclient;

    public OrderDto createOrder(UUID userId) {
        log.info("Received request to create order for user: {}", userId);



    }
}
