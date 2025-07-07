package com.project.keyboard.system.impl;

import com.project.keyboard.dto.request.ProductCategoryRequestDTO;
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

        return categories.stream().map(category -> {
            String parentName = category.getParent() != null ? category.getParent().getName() : null;
            return new ProductCategoryDTO(
                    category.getName(),
                    category.getDescription(),
                    parentName
            );
        }).collect(Collectors.toList());
    }

    @Override
    public ProductCategory createCategory(ProductCategoryRequestDTO dto){
        if (categoryRepository.existsByName(dto.getName())){
            throw new RuntimeException("Tên danh mục đã tồn tại!");
        }

        ProductCategory parent = null;
        if (dto.getParentName() != null && !dto.getParentName().trim().isEmpty()) {
            parent = categoryRepository.findByName(dto.getParentName());
            if (parent == null) {
                throw new RuntimeException("Không tìm thấy danh mục cha: " + dto.getParentName());
            }
        }

        ProductCategory newCategory = new ProductCategory();
        newCategory.setName(dto.getName());
        newCategory.setParent(parent);
        newCategory.setDescription(dto.getDescription());

        categoryRepository.insert(newCategory);
        return newCategory;
    }

    @Override
    public ProductCategory updateCategory(ProductCategoryRequestDTO dto, int id){
        ProductCategory existing = categoryRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }

        ProductCategory parent = null;
        if (dto.getParentName() != null && !dto.getParentName().trim().isEmpty()) {
            parent = categoryRepository.findByName(dto.getParentName());
            if (parent == null) {
                throw new RuntimeException("Không tìm thấy danh mục cha: " + dto.getParentName());
            }
        }

        existing.setName(dto.getName());
        existing.setParent(parent);
        existing.setDescription(dto.getDescription());

        categoryRepository.update(id, existing);
        return existing;
    }

    @Override
    public void deleteCategory(int id){
        ProductCategory existing = categoryRepository.findById(id);
        if (existing == null) {
            throw new RuntimeException("Không tìm thấy danh mục!");
        }
        categoryRepository.deleteById(id);
    }
}
