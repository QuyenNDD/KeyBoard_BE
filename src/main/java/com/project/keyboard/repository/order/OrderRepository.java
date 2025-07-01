package com.project.keyboard.repository.order;

import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;

import java.util.List;

public interface OrderRepository {
    List<MonthlyOrderCount> getMonthlyOrderCount(int year);
}
