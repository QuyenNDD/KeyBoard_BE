package com.project.keyboard.dto.response.revenue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyOrderCount {
    private int month;
    private int totalOrders;
}
