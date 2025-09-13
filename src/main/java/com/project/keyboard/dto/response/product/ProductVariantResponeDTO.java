package com.project.keyboard.dto.response.product;

import com.project.keyboard.dto.request.ProductImgDTO;
import com.project.keyboard.dto.request.VariantImgDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductVariantResponeDTO {
    private int variantId;
    private String color;
    private double price;
    private int stockQuantity;
    private String sku;
    private boolean deleted;
    private VariantImgDTO variantImg;
}
