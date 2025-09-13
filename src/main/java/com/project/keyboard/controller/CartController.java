package com.project.keyboard.controller;

import com.cloudinary.Api;
import com.project.keyboard.dto.request.AddToCartRequest;
import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.cart.CartUserResponse;
import com.project.keyboard.dto.response.cart.TotalCartDTO;
import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.page.PagedResponse;
import com.project.keyboard.system.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/getTotalCart")
    public ResponseEntity<ApiResponse<Map<String, List<TotalCartDTO>>>> getDashboardSummary(HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
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


    // Thêm vào giỏ hàng có đăng nhập
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<String>> addToCart(
        @RequestBody AddToCartRequest add,
        HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            if (userId == null){
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new  ApiResponse<>("Chưa đăng nhập", 401, "unauthorized", null, null));
            }
            String message =  cartService.addToCart(userId, add.getVariantId(), add.getQuantity());
            return ResponseEntity.ok(new ApiResponse<>(message, 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/getListCartUser")
    public ResponseEntity<ApiResponse<List<CartUserResponse>>> getListCartUser(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("Bạn chưa đăng nhập", 401, "unauthorized", null, null));
            }
            List<CartUserResponse> cartUserResponses = cartService.getListCartBelongUser(userId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Danh sach don hang", 200, "success", cartUserResponses, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{cartId}")
    public ResponseEntity<ApiResponse<String>> deleteCart(@PathVariable int cartId, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            System.out.println(userId);
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("Bạn chưa đăng nhập", 401, "unauthorized", null, null));
            }
            cartService.deleteCart(userId, cartId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Xóa thành công", 200, "success", null, null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }
}
