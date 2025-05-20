package com.socompany.productservice.service;

import com.socompany.productservice.mapper.ProductMapper;
import com.socompany.productservice.persistant.dto.ProductDto;
import com.socompany.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;


    public List<ProductDto> findAllProducts() {
        log.info("Getting all products");
        return productRepository.findAllByOrderByNameAsc().stream().map(productMapper::toDto).toList();
    }

    public ProductDto saveProduct(ProductDto productDto) {
        log.info("Saving product: {}", productDto);
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    public Optional<ProductDto> findProductById(UUID id) {
        log.info("Finding product by id: {}", id);
        return productRepository.findById(id)
                .map(productMapper::toDto);
    }

    public Optional<ProductDto> updateProduct(UUID id,
                                              ProductDto productDto,
                                              UUID authenticatedUserId,
                                              List<String> userRoles) throws AccessDeniedException {
        log.info("Trying to find product, with id: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        log.info("Product found: {}", product);
        boolean isAdmin = isAdmin(userRoles);
        boolean isOwner = product.getOwnerId().equals(authenticatedUserId);

        if(!isAdmin && !isOwner){
            log.error("User {} is not admin and is not owner of product with id: {}", authenticatedUserId, id);
            throw new AccessDeniedException("Access denied");
        }
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        product.setLastModifiedDate(Instant.now());
        log.info("Product updated, trying to save product to db:");

        var result = Optional.of(productMapper.toDto(productRepository.save(product)));
        productRepository.flush();
        return result;
    }

    public boolean deleteProductById(UUID id, UUID authenticatedUserId, List<String> userRoles) throws AccessDeniedException {
        log.info("Deleting product: {}", id);
        var product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Product with id: {} is not found.", id);
                    return new IllegalArgumentException("Product not found");
                });
        boolean isAdmin = isAdmin(userRoles);
        boolean isOwner = product.getOwnerId().equals(authenticatedUserId);

        if (!isAdmin && !isOwner) {
            log.error("User {} is not admin and is not owner of product with id: {}", authenticatedUserId, id);
            throw new AccessDeniedException("Access denied");
        }
        productRepository.delete(product);
        productRepository.flush();
        log.info("Product deleted.");
        return true;
    }

    boolean isAdmin(List<String> userRoles) {
        return userRoles.stream().anyMatch(role -> role.equals("ROLE_ADMIN"));
    }

}
