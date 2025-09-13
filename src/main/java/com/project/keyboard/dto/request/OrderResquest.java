package com.project.keyboard.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderResquest {
    private List<OrderItemRequest> items;
    private String phone;
    private String address;
    private String email;
    private String fullName;
}
