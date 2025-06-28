package com.project.keyboard.repository.category;

import com.project.keyboard.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryRepository {
    List<ProductCategory> getListProductCategory();
    ProductCategory findByName(String name);
}
