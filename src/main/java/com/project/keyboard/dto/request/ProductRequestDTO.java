package com.project.keyboard.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequestDTO {
    private String name;
    private String brand;
    private String category;
    private String description;
    private boolean status;
    private List<ProductVariantRequestDTO> variants;
}
