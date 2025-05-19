package com.socompany.productservice.repository;

import com.socompany.productservice.persistant.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    public List<Product> findAllByOrderByNameAsc();

    public Optional<Product> findById(UUID id);
}
