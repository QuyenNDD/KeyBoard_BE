package com.project.keyboard.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ProductUpdateDTO {
    private int productId;
    private String name;
    private String brand;
    private String description;
    private Integer categoryId;
    private ProductImgDTO productImg;
    private List<ProductVariantUpdateDTO> variants;
    private String imgs;
}
