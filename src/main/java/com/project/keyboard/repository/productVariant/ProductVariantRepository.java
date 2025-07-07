package com.project.keyboard.repository.productVariant;

import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.request.ProductVariantUpdateDTO;
import com.project.keyboard.entity.ProductVariant;

import java.util.List;

public interface ProductVariantRepository {
    void save(ProductVariant variant);
    void deleteById(int variantId);
    ProductVariant findById(int variantId);
    void update(ProductVariant productVariant);
    void updateVariant(ProductVariantUpdateDTO v);
    void insertVariant(ProductVariantUpdateDTO v, int productId);
    void deleteVariantById(int variantId);
    List<ProductVariant> findByProductId(int productId);
}
