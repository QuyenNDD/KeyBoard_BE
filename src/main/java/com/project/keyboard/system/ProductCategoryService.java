package com.project.keyboard.system;

import com.project.keyboard.dto.response.category.ProductCategoryDTO;

import java.util.List;

public interface ProductCategoryService {
    List<ProductCategoryDTO> getListProductCategory();
}
