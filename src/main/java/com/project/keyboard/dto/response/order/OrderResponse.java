package com.project.keyboard.dto.response.order;

import com.project.keyboard.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private int id;
    private String fullName;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;
    private String phone;
    private String address;
    private String email;
    private OrderStatus status;

    private List<OrderDetailResponse> orderDetails;
}
