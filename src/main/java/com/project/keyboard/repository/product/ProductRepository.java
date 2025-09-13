package com.project.keyboard.repository.product;

import com.project.keyboard.dto.request.ProductUpdateDTO;
import com.project.keyboard.dto.response.FilterProductResponse;
import com.project.keyboard.dto.response.product.NewProductDTO;
import com.project.keyboard.dto.response.product.ProductResponeDTO;
import com.project.keyboard.dto.response.revenue.TopSellingProductDTO;
import com.project.keyboard.entity.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getListProduct(int page, int size);
    int countProducts();
    int save(Product product);
    Product findById(int id);
    boolean softDeleteProductById(int id);

    void update(Product product);
    void updateProduct(ProductUpdateDTO dto);
    List<TopSellingProductDTO> getTopSellingProduct(int limit);
    List<NewProductDTO> getNewProduct(int limit);
    Product findProductById(int id);
    List<FilterProductResponse> filterProduct(String price, int page, int size);

    int countFilterProduct(String filterQuery);
}
