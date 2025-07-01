package com.project.keyboard.system;

import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;

import java.util.List;

public interface OrderService {
    List<Integer> getMonthlyOrderCount(int year);
}
