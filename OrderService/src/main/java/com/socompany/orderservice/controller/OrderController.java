package com.socompany.orderservice.controller;

import com.socompany.orderservice.client.UserServiceClient;
import com.socompany.orderservice.persistant.dto.OrderRequestDto;
import com.socompany.orderservice.persistant.dto.OrderResponseDto;
import com.socompany.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserServiceClient userServiceClient;

    @PostMapping("/get")
    public ResponseEntity<OrderResponseDto> createOrder(Authentication authentication,
                                                        @RequestBody OrderRequestDto orderRequestDto) {
        log.info("Received request to create order for user: {}", authentication.getName());
        var user = userServiceClient.getUserByUsername(authentication.getName());

        var response = orderService.createOrder(user.getId(), orderRequestDto);
        log.info("Order created: {}", response);
        return ResponseEntity.ok(response);
    }

}
