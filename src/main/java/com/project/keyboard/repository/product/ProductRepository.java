package com.project.keyboard.repository.product;

import com.project.keyboard.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getListProduct();
    int save(Product product);
    Product findById(int id);
    boolean softDeleteProductById(int id);

    void update(Product product);
}
