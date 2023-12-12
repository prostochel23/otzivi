package com.example.otzivi.repositories;

import com.example.otzivi.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByTitle(String title, Pageable pageable);
    Page<Product> findByCategory(String category, Pageable pageable);
    Page<Product> findAll(Pageable pageable);
}
