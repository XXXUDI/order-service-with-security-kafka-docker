package com.socompany.orderservice.service;

import com.socompany.orderservice.mapper.OrderMapper;
import com.socompany.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final RetryableTaskService retryableTaskService;
}
