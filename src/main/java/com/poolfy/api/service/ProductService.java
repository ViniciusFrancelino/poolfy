package com.poolfy.api.service;

import com.poolfy.api.dto.product.ProductDtos.BrandRequest;
import com.poolfy.api.dto.product.ProductDtos.BrandResponse;
import com.poolfy.api.dto.product.ProductDtos.CreateOrUpdateProductRequest;
import com.poolfy.api.dto.product.ProductDtos.ProductResponse;
import com.poolfy.api.entity.Product;
import com.poolfy.api.entity.ProductBrand;
import com.poolfy.api.entity.enums.ActivityType;
import com.poolfy.api.exception.NotFoundException;
import com.poolfy.api.repository.ProductBrandRepository;
import com.poolfy.api.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductBrandRepository productBrandRepository;
    private final ActivityService activityService;
    private final UserService userService;

    public List<ProductResponse> listByUser(Long userId) {
        userService.findUser(userId);
        return productRepository.findByUserIdOrderByNameAsc(userId).stream().map(this::toResponse).toList();
    }

    public ProductResponse getById(Long productId) {
        return toResponse(findProduct(productId));
    }

    public ProductResponse create(CreateOrUpdateProductRequest request) {
        userService.findUser(request.getUserId());

        Product product = new Product();
        fillProduct(product, request);
        product = productRepository.save(product);

        activityService.register(product.getUserId(), ActivityType.PRODUCT_CREATED, product.getId(),
                "Produto cadastrado: " + product.getName());

        return toResponse(product);
    }

    public ProductResponse update(Long productId, CreateOrUpdateProductRequest request) {
        Product product = findProduct(productId);
        fillProduct(product, request);
        product = productRepository.save(product);

        activityService.register(product.getUserId(), ActivityType.PRODUCT_UPDATED, product.getId(),
                "Produto atualizado: " + product.getName());

        return toResponse(product);
    }

    public void delete(Long productId) {
        Product product = findProduct(productId);
        productRepository.delete(product);
    }

    public List<BrandResponse> listBrands(Long userId) {
        userService.findUser(userId);
        return productBrandRepository.findByUserIdOrderByBrandNameAsc(userId)
                .stream()
                .map(this::toBrandResponse)
                .toList();
    }

    public BrandResponse createBrand(BrandRequest request) {
        userService.findUser(request.getUserId());

        ProductBrand brand = new ProductBrand();
        brand.setUserId(request.getUserId());
        brand.setBrandName(request.getBrandName());
        brand = productBrandRepository.save(brand);

        return toBrandResponse(brand);
    }

    public Product findProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado."));
    }

    private void fillProduct(Product product, CreateOrUpdateProductRequest request) {
        product.setUserId(request.getUserId());
        product.setBrandId(request.getBrandId());
        product.setName(request.getName());
        product.setProductType(request.getProductType());
        product.setDosagePerM3(request.getDosagePerM3());
        product.setDosageUnit(request.getDosageUnit());
        product.setDescription(request.getDescription());
        product.setLowStockAlertEnabled(request.getLowStockAlertEnabled());
        product.setLowStockThreshold(request.getLowStockThreshold());
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setUserId(product.getUserId());
        response.setBrandId(product.getBrandId());
        response.setBrandName(resolveBrandName(product.getBrandId()));
        response.setName(product.getName());
        response.setProductType(product.getProductType());
        response.setDosagePerM3(product.getDosagePerM3());
        response.setDosageUnit(product.getDosageUnit());
        response.setDescription(product.getDescription());
        response.setLowStockAlertEnabled(product.getLowStockAlertEnabled());
        response.setLowStockThreshold(product.getLowStockThreshold());
        return response;
    }

    private String resolveBrandName(Long brandId) {
        if (brandId == null) return null;
        return productBrandRepository.findById(brandId).map(ProductBrand::getBrandName).orElse(null);
    }

    private BrandResponse toBrandResponse(ProductBrand brand) {
        BrandResponse response = new BrandResponse();
        response.setId(brand.getId());
        response.setUserId(brand.getUserId());
        response.setBrandName(brand.getBrandName());
        return response;
    }
}
