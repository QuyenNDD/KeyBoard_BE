package com.project.keyboard.dto.response.user;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserOrderResponse {
    private int id;
    private String address;
    private BigDecimal totalAmount;
    private LocalDateTime orderDate;
    private String status;
}
