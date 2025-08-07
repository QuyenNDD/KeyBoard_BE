package com.project.keyboard.dto.response.cart;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CartUserResponse {
    private int cartId;
    private String name;
    private String color;
    private String brand;
    private String img;
    private BigDecimal price;
    private int quantity;
    private LocalDateTime addedAt;
}
