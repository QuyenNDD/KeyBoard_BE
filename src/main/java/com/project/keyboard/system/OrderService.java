package com.project.keyboard.system;

import com.project.keyboard.dto.request.GuestOrderRequest;
import com.project.keyboard.dto.request.OrderResquest;
import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.revenue.DayOrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.dto.response.revenue.OrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.WeekOrderRevenueDTO;
import com.project.keyboard.dto.response.user.UserOrderResponse;
import com.project.keyboard.entity.Order;
import com.project.keyboard.enums.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

public interface OrderService {
    List<Integer> getMonthlyOrderCount(int year);
    List<OrderResponse> getListOrders(int page, int size);
    int countOrders();
    List<OrderResponse> getOrderByUser(int userId, int page, int size);
    List<OrderResponse> getOrdersByExactDate(String date, int page, int size);
    int countOrdersByExactDate(String date);
    OrderResponse getOrderById(int id);
    void setOrderStatus(int id, OrderStatus status);
    DayOrderRevenueDTO getDayOrderRevenue(LocalDate date);
    OrderRevenueDTO getRevenueByMonth(int year, Integer month);
    OrderRevenueDTO getRevenueByYear(int year);
    List<WeekOrderRevenueDTO> getRevenueByWeek(LocalDate start, LocalDate end);
    List<UserOrderResponse> getOrdersByUser(int userId, int page, int size);
    int countOrdersByUser(int userId);
    OrderResponse getOrderByIdForUser(int orderId, Integer userId);
    String placeOrder(int userId, OrderResquest orderResquest);
    String placeGuestOrder(GuestOrderRequest guestOrderRequest);
}
