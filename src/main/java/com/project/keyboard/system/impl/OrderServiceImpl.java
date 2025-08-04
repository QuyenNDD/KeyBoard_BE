package com.project.keyboard.system.impl;

import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.revenue.DayOrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.dto.response.revenue.OrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.WeekOrderRevenueDTO;
import com.project.keyboard.dto.response.user.UserOrderResponse;
import com.project.keyboard.entity.Order;
import com.project.keyboard.enums.OrderStatus;
import com.project.keyboard.repository.order.OrderRepository;
import com.project.keyboard.system.OrderService;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Override
    public List<OrderResponse> getListOrders(int page, int size) {
        return orderRepository.getListOrder(page, size);
    }


    @Override
    public int countOrders(){
        return orderRepository.countOrders();
    }

    @Override
    public List<OrderResponse> getOrderByUser(int userId, int page, int size){
        return orderRepository.getOrdersByUserId(userId, page, size);
    }

    @Override
    public List<OrderResponse> getOrdersByExactDate(String date, int page, int size){
        return orderRepository.getOrdersByExactDate(date, page, size);
    }
    @Override
    public int countOrdersByExactDate(String date){
        return orderRepository.countOrdersByExactDate(date);
    }

    @Override
    public OrderResponse getOrderById(int orderId) {
        return orderRepository.getOrderById(orderId);
    }

    @Override
    public void setOrderStatus(int orderId, OrderStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("OrderStatus must not be null");
        }
        orderRepository.updateOrderStatus(orderId, status.name());
    }

    @Override
    public DayOrderRevenueDTO getDayOrderRevenue(LocalDate date) {
        return orderRepository.getRevenueByDay(date);
    }

    @Override
    public OrderRevenueDTO getRevenueByMonth(int year, Integer month){
        return orderRepository.getRevenueByMonth(year, month);
    }

    @Override
    public OrderRevenueDTO getRevenueByYear(int year){
        return orderRepository.getRevenueByYear(year);
    }

    @Override
    public List<WeekOrderRevenueDTO> getRevenueByWeek(LocalDate start, LocalDate end){
        return orderRepository.getRevenueByWeek(start, end);
    }

    @Override
    public List<UserOrderResponse> getOrdersByUser(int userId, int page, int size){
        return orderRepository.getOrdersByUser(userId, page, size);
    }
    @Override
    public int countOrdersByUser(int userId){
        return orderRepository.countOrdersByUser(userId);
    }

    @Override
    public OrderResponse getOrderByIdForUser(int orderId, Integer userId){
        return orderRepository.getOrderByIdForUser(orderId, userId);
    }
}
