package com.poolfy.api.repository;

import com.poolfy.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByUserIdOrderByNameAsc(Long userId);
}
