package com.poolfy.api.repository;

import com.poolfy.api.entity.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, Long> {
    List<ProductBrand> findByUserIdOrderByBrandNameAsc(Long userId);
}
