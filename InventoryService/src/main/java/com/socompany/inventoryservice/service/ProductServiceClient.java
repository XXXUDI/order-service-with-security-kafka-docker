package com.socompany.inventoryservice.service;

import com.socompany.inventoryservice.persistant.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Deprecated
@FeignClient(name = "product-service", path = "/api/product")
public interface ProductServiceClient {

    @GetMapping
    List<ProductDto> findAll();

    @GetMapping("/{id}")
    ProductDto findById(@PathVariable("id") UUID id);

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    ProductDto saveProduct(@RequestBody ProductDto productDto);

    @PutMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    ProductDto updateProduct(@RequestBody ProductDto productDto,
                             @RequestParam("id") String id);

    @DeleteMapping("/remove")
    Boolean deleteProduct(@RequestParam("id") String id);
}

