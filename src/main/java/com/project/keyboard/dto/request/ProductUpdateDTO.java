package com.project.keyboard.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductUpdateDTO {
    private int productId;
    private String name;
    private String brand;
    private String description;
    private boolean status;
    private String category; // tên danh mục
    private List<ProductVariantUpdateDTO> variants;
}
