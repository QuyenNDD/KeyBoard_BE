package com.project.keyboard.dto.request;

import lombok.Data;

@Data
public class OrderItemRequest {
    private int cartId;
    private int quantity;
}
