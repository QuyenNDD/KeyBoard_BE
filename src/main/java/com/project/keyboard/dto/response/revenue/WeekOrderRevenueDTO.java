package com.project.keyboard.dto.response.revenue;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WeekOrderRevenueDTO {
    private Integer week;              // Số thứ tự tuần (ISO)
    private LocalDate weekStartDate;   // Ngày bắt đầu tuần
    private Long totalOrders;
    private BigDecimal totalRevenue;
}
