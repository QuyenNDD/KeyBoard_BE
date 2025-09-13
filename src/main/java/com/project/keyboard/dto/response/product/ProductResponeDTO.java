package com.project.keyboard.dto.response.product;

import com.project.keyboard.dto.request.ProductImgDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductResponeDTO {
    private int productId;
    private String name;
    private String brand;
    private BigDecimal minPrice;
    private int categoryId;
    private String description;
    private String category;
    private ProductImgDTO productImg;
    private List<ProductVariantResponeDTO> variants;
}
