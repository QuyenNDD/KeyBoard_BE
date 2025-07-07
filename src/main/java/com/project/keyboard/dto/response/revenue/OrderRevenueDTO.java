package com.project.keyboard.dto.response.revenue;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRevenueDTO {
    private String date; // YYYY-MM-DD
    private Integer week;
    private Integer month;
    private Integer year;
    private BigDecimal totalRevenue;
    private Long totalOrders;
}
