package com.project.keyboard.dto.response.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailResponse {
    private int variantId;
    private String productName;
    private String color;
    private int quantity;
    private BigDecimal price;
    private String img;
}
