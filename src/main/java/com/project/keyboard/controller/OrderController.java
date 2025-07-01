package com.project.keyboard.controller;

import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.system.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/monthly-order-count")
    public ResponseEntity<ApiResponse<List<Integer>>> getMonthlyOrderCount(@RequestParam int year) {
        try {
            List<Integer> monthlyOrderCount = orderService.getMonthlyOrderCount(year);
            return ResponseEntity.ok(new ApiResponse<>("Tong hop don hang 12 thang cua 1 nam", 200, "success", monthlyOrderCount, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

}
