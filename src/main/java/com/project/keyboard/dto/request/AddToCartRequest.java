package com.project.keyboard.dto.request;

import lombok.Data;

@Data
public class AddToCartRequest {
    private int variantId;
    private int quantity;
}
