package com.project.keyboard.controller;

import com.project.keyboard.dto.response.api.ApiResponse;

import com.project.keyboard.dto.response.page.PagedResponse;
import com.project.keyboard.dto.response.user.UserDTO;

import com.project.keyboard.dto.response.user.UserOrderResponse;

import com.project.keyboard.system.OrderService;
import com.project.keyboard.system.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/getListUser")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getListUser(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            List<UserDTO> users = userService.getListUser(page, size);
            int totalElements = userService.countUsers();
            int totalPages = (int) Math.ceil((double) totalElements / size);

            PagedResponse<UserDTO> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(users);
            pagedResponse.setTotalPages(totalPages);
            pagedResponse.setTotalElements(totalElements);
            pagedResponse.setSize(size);
            pagedResponse.setPage(page);
            return ResponseEntity.ok(
                    new ApiResponse<>("Lấy danh sách người dùng thành công", 200, "success", users, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getUserDetail(
            @PathVariable int id,
            HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(403).body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            UserDTO user = userService.getUserDetail(id);
            return ResponseEntity.ok(new ApiResponse<>("Lấy chi tiết user thành công", 200, "success", user, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }

    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<ApiResponse<List<UserOrderResponse>>> getUserOrders(
            @PathVariable int id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(403).body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }

            List<UserOrderResponse> orders = orderService.getOrdersByUser(id, page, size);
            int totalElements = orderService.countOrdersByUser(id);
            int totalPages = (int) Math.ceil((double) totalElements / size);

            PagedResponse<UserOrderResponse> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(orders);
            pagedResponse.setTotalElements(totalElements);
            pagedResponse.setTotalPages(totalPages);
            pagedResponse.setSize(size);
            pagedResponse.setPage(page);

            return ResponseEntity.ok(new ApiResponse<>("Lấy danh sách đơn hàng thành công", 200, "success", orders, null));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @PostMapping("/banUser/{userId}")
    public ResponseEntity<ApiResponse<Void>> banUser(@PathVariable int userId,
                                                     HttpServletRequest request){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            userService.banUser(userId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Khóa tài khoản thành công", 200, "success", null, null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @PostMapping("/unBanUser/{userId}")
    public ResponseEntity<ApiResponse<Void>> unBanUser(@PathVariable int userId,
                                                       HttpServletRequest request){
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            userService.unBanUser(userId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Mở tài khoản thành công", 200, "success", null, null)
            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }
}
