package com.socompany.productservice.controller;

import com.socompany.productservice.persistant.dto.ProductDto;
import com.socompany.productservice.service.ProductService;
import com.socompany.productservice.service.UserServiceClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final UserServiceClient userServiceClient;
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        log.info("Received request to get all products");
        return ResponseEntity.ok(productService.findAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable UUID id) {
        log.info("Received request to get product by id: {}", id);
        return ResponseEntity.ok(productService.findProductById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));
    }

    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@Valid @RequestBody ProductDto productDto,
                                                  Authentication authentication) {
        log.info("Received request to save product: {}", productDto);
        String authenticatedUser = authentication.getName();
        var user = userServiceClient.getUserByUsername(authenticatedUser);
        log.info("User found: {}", user);
        productDto.setOwnerId(user.getId());

        return ResponseEntity.ok(productService.saveProduct(productDto));
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto,
                                                  @RequestParam UUID id,
                                                  Authentication authentication) throws AccessDeniedException {
        log.info("Received request to update product: {}", productDto);

        String authenticatedUser = authentication.getName();
        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var user = userServiceClient.getUserByUsername(authenticatedUser);

        return ResponseEntity.ok(productService.updateProduct(id, productDto, user.getId(), userRoles)
                .orElseThrow(() -> new IllegalArgumentException("Product not found")));
    }

    @DeleteMapping
    public ResponseEntity<Boolean> deleteProduct(@RequestParam UUID id,
                                                 Authentication authentication) throws AccessDeniedException {
        log.info("Received update request for id: {} by user: {}, authorities: {}", id, authentication.getName(), authentication.getAuthorities());
        String authenticatedUser = authentication.getName();
        List<String> userRoles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var user = userServiceClient.getUserByUsername(authenticatedUser);

        return ResponseEntity.ok(productService.deleteProductById(id, user.getId(), userRoles));

    }
}
