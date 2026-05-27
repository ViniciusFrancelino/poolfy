package com.poolfy.api.controller;

import com.poolfy.api.dto.common.ApiMessageResponse;
import com.poolfy.api.dto.product.ProductDtos.BrandRequest;
import com.poolfy.api.dto.product.ProductDtos.CreateOrUpdateProductRequest;
import com.poolfy.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products")
    public Object listProducts(@RequestParam Long userId) {
        return productService.listByUser(userId);
    }

    @GetMapping("/products/{productId}")
    public Object getProduct(@PathVariable Long productId) {
        return productService.getById(productId);
    }

    @PostMapping("/products")
    public Object createProduct(@RequestBody CreateOrUpdateProductRequest request) {
        return productService.create(request);
    }

    @PutMapping("/products/{productId}")
    public Object updateProduct(@PathVariable Long productId, @RequestBody CreateOrUpdateProductRequest request) {
        return productService.update(productId, request);
    }

    @DeleteMapping("/products/{productId}")
    public Object deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        return new ApiMessageResponse("Produto removido com sucesso.");
    }

    @GetMapping("/brands")
    public Object listBrands(@RequestParam Long userId) {
        return productService.listBrands(userId);
    }

    @PostMapping("/brands")
    public Object createBrand(@RequestBody BrandRequest request) {
        return productService.createBrand(request);
    }
}
