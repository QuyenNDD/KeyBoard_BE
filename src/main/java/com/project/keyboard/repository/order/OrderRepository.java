package com.project.keyboard.repository.order;

import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.revenue.DayOrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.dto.response.revenue.OrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.WeekOrderRevenueDTO;
import com.project.keyboard.dto.response.user.UserOrderResponse;
import com.project.keyboard.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    List<MonthlyOrderCount> getMonthlyOrderCount(int year);
    List<OrderResponse> getListOrder(int page, int size);
    int countOrders();
    List<OrderResponse> getOrdersByExactDate(String date, int page, int size);
    int countOrdersByExactDate(String date);
    OrderResponse getOrderById(int id);
    void updateOrderStatus(int id, String status);
    DayOrderRevenueDTO getRevenueByDay(LocalDate date);
    OrderRevenueDTO getRevenueByMonth(int year, Integer month);
    OrderRevenueDTO getRevenueByYear(int year);
    List<WeekOrderRevenueDTO> getRevenueByWeek(LocalDate start, LocalDate end);
    List<UserOrderResponse> getOrdersByUser(int userId, int page, int size);
    int countOrdersByUser(int userId);
}
