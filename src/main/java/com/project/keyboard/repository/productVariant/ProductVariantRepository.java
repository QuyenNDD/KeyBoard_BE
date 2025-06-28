package com.project.keyboard.repository.productVariant;

import com.project.keyboard.entity.ProductVariant;

public interface ProductVariantRepository {
    void save(ProductVariant variant);

    void deleteById(int variantId);
    ProductVariant findById(int variantId);
    void update(ProductVariant productVariant);
}
