package com.project.keyboard.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class GuestOrderRequest {
    private int variantId;
    private int quantity;
    private String email;
    private String phone;
    private String address;
}
