package com.project.keyboard.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductRequestDTO {
    private String name;
    private String brand;
    private int categoryId;
    private String description;
    private boolean status;

    private String imgs;
    private List<ProductVariantRequestDTO> variants;
}
