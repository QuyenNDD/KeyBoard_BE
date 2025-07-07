package com.project.keyboard.system;

import com.project.keyboard.dto.request.ProductCategoryRequestDTO;
import com.project.keyboard.dto.response.category.ProductCategoryDTO;
import com.project.keyboard.entity.ProductCategory;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategoryDTO> getListProductCategory();
    ProductCategory createCategory(ProductCategoryRequestDTO dto);
    ProductCategory updateCategory(ProductCategoryRequestDTO dto, int id);
    void deleteCategory(int id);
}
