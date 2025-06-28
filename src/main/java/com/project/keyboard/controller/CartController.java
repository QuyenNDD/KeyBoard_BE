package com.project.keyboard.controller;

import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.cart.TotalCartDTO;
import com.project.keyboard.system.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/getTotalCart")
    public ResponseEntity<ApiResponse<Map<String, List<TotalCartDTO>>>> getDashboardSummary() {
        try {
            List<TotalCartDTO> carts = cartService.getTotalCart();
            Map<String, List<TotalCartDTO>> responseData = Map.of("totalCarts", carts);

            ApiResponse<Map<String, List<TotalCartDTO>>> apiResponse = new ApiResponse<>(
                    "Lấy dữ liệu thành công",
                    200,
                    "success",
                    responseData,
                    null
            );
            return ResponseEntity.ok(apiResponse);

        } catch (Exception e) {
            ApiResponse<Map<String, List<TotalCartDTO>>> errorResponse = new ApiResponse<>(
                    "Đã xảy ra lỗi khi lấy dữ liệu",
                    500,
                    "error",
                    null,
                    e.getMessage()
            );
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

}
