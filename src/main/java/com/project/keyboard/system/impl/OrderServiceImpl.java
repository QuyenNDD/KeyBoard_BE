package com.project.keyboard.system.impl;

import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.repository.order.OrderRepository;
import com.project.keyboard.system.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Integer> getMonthlyOrderCount(int year) {
        List<MonthlyOrderCount> rawResult = orderRepository.getMonthlyOrderCount(year);

        // Map: month -> totalOrders
        Map<Integer, Integer> monthMap = new HashMap<>();
        for (MonthlyOrderCount row : rawResult) {
            monthMap.put(row.getMonth(),row.getTotalOrders());
        }

        // Mảng 12 phần tử
        List<Integer> result = new ArrayList<>();
        for (int m = 1; m <= 12; m++) {
            result.add(monthMap.getOrDefault(m, 0));
        }

        return result;
    }
}
