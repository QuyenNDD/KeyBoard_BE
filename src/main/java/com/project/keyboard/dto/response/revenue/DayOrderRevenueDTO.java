package com.project.keyboard.dto.response.revenue;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DayOrderRevenueDTO {
    private String date;         // yyyy-MM-dd
    private BigDecimal totalRevenue;
    private Long totalOrders;
}
