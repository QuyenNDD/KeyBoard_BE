package com.project.keyboard.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductVariantRequestDTO {
    private String color;
    private BigDecimal price;
    private int stockQuantity;
    private String img;
    private String sku;
}
