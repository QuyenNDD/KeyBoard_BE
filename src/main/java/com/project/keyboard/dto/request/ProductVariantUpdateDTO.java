package com.project.keyboard.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductVariantUpdateDTO {
    private int variantId; // null nếu là biến thể mới
    private String color;
    private BigDecimal price;
    private int stockQuantity;
    private String img;
    private String sku;
    private boolean deleted;
    private boolean replaceImage;
}
