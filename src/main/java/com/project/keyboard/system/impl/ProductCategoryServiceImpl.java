package com.project.keyboard.system.impl;

import com.project.keyboard.dto.response.category.ProductCategoryDTO;
import com.project.keyboard.entity.ProductCategory;
import com.project.keyboard.repository.category.ProductCategoryRepository;
import com.project.keyboard.system.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    @Autowired
    private ProductCategoryRepository categoryRepository;

    @Override
    public List<ProductCategoryDTO> getListProductCategory() {
        List<ProductCategory> categories = categoryRepository.getListProductCategory();
        return categories.stream()
                .map(category -> new ProductCategoryDTO(category.getName(), category.getDescription()))
                .collect(Collectors.toList());
    }
}
