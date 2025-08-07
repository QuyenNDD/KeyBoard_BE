package com.project.keyboard.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private int cartId;
    private int userId;
    private int variantId;
    private int quantity;
}
