package com.project.keyboard.system;

import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.revenue.DayOrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.dto.response.revenue.OrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.WeekOrderRevenueDTO;
import com.project.keyboard.entity.Order;
import com.project.keyboard.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface OrderService {
    List<Integer> getMonthlyOrderCount(int year);
    List<OrderResponse> getListOrders();
    OrderResponse getOrderById(int id);
    void setOrderStatus(int id, OrderStatus status);
    DayOrderRevenueDTO getDayOrderRevenue(LocalDate date);
    OrderRevenueDTO getRevenueByMonth(int year, Integer month);
    OrderRevenueDTO getRevenueByYear(int year);
    List<WeekOrderRevenueDTO> getRevenueByWeek(LocalDate start, LocalDate end);
}
