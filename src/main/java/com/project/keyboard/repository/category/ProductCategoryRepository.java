package com.project.keyboard.repository.category;

import com.project.keyboard.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryRepository {
    List<ProductCategory> getListProductCategory();
    ProductCategory findByName(String name);
    boolean existsByName(String name);
    int insert(ProductCategory productCategory);
    ProductCategory findById(int id);
    int update(int id, ProductCategory productCategory);
    int deleteById(int id);
}
