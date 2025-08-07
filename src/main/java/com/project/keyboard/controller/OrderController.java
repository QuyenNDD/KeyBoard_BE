package com.project.keyboard.controller;

import com.project.keyboard.dto.request.OrderResquest;
import com.project.keyboard.dto.response.api.ApiResponse;
import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.page.PagedResponse;
import com.project.keyboard.dto.response.revenue.DayOrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.dto.response.revenue.OrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.WeekOrderRevenueDTO;
import com.project.keyboard.entity.Order;
import com.project.keyboard.enums.OrderStatus;
import com.project.keyboard.system.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/monthly-order-count")
    public ResponseEntity<ApiResponse<List<Integer>>> getMonthlyOrderCount(@RequestParam int year, HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            List<Integer> monthlyOrderCount = orderService.getMonthlyOrderCount(year);
            return ResponseEntity.ok(new ApiResponse<>("Tong hop don hang 12 thang cua 1 nam", 200, "success", monthlyOrderCount, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @GetMapping("/getListOrder")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getListOrders(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try{
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            List<OrderResponse> orders = orderService.getListOrders(page, size);
            int totalElements = orderService.countOrders();
            int totalPages = (int) Math.ceil((double) totalElements / size);
            PagedResponse<OrderResponse> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(orders);
            pagedResponse.setTotalPages(totalPages);
            pagedResponse.setTotalElements(totalElements);
            pagedResponse.setSize(size);
            pagedResponse.setPage(page);
            return ResponseEntity.ok(
                    new ApiResponse<>("Danh sach don hang", 200, "success", orders, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/getListOrder/user")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getListUserOrders(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try{
            Integer userId = (Integer) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("Bạn chưa đăng nhập", 401, "unauthorized", null, null));
            }

            List<OrderResponse> orders = orderService.getOrderByUser(userId, page, size);
            int totalElements = orderService.countOrdersByUser(userId);
            int totalPages = (int) Math.ceil((double) totalElements / size);
            PagedResponse<OrderResponse> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(orders);
            pagedResponse.setTotalPages(totalPages);
            pagedResponse.setTotalElements(totalElements);
            pagedResponse.setSize(size);
            pagedResponse.setPage(page);
            return ResponseEntity.ok(
                    new ApiResponse<>("Danh sach don hang", 200, "success", orders, null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/getListOrderByExactDate")
    public ResponseEntity<ApiResponse<PagedResponse<OrderResponse>>> getListOrdersByExactDate(
            @RequestParam String date, // định dạng yyyy-MM-dd
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }

            List<OrderResponse> orders = orderService.getOrdersByExactDate(date, page, size);
            int totalElements = orderService.countOrdersByExactDate(date);
            int totalPages = (int) Math.ceil((double) totalElements / size);

            PagedResponse<OrderResponse> pagedResponse = new PagedResponse<>();
            pagedResponse.setContent(orders);
            pagedResponse.setTotalPages(totalPages);
            pagedResponse.setTotalElements(totalElements);
            pagedResponse.setSize(size);
            pagedResponse.setPage(page);

            return ResponseEntity.ok(
                    new ApiResponse<>("Danh sách đơn hàng ngày " + date, 200, "success", pagedResponse, null)
            );

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage())
            );
        }
    }

    @GetMapping("/getOrderById/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable int orderId, HttpServletRequest request) {
        try{
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            OrderResponse order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(
                    new ApiResponse<>("Chi tiet don hang", 200, "success", order, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/getOrderById/user/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getUserOrderById(@PathVariable int orderId, HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("Người dùng chưa đăng nhập", 401, "unauthorized", null, null));
            }

            OrderResponse order = orderService.getOrderByIdForUser(orderId, userId);
            if (order == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập đơn hàng này", 403, "forbidden", null, null));
            }

            return ResponseEntity.ok(new ApiResponse<>("Chi tiết đơn hàng", 200, "success", order, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @PutMapping("/updateOrderStatus/{orderId}")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(@PathVariable int orderId, @RequestParam OrderStatus status, HttpServletRequest request) {
        try{
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            orderService.setOrderStatus(orderId, status);
            return ResponseEntity.ok(
                    new ApiResponse<>("Cập nhật trạng thái đơn hàng thành công", 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse<>("Đã xảy ra lỗi", 500, "error", null, e.getMessage()));
        }
    }

    @GetMapping("/by-day")
    public ResponseEntity<ApiResponse<DayOrderRevenueDTO>> getRevenueByDay(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            DayOrderRevenueDTO result = orderService.getDayOrderRevenue(date);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thống kê theo ngày thành công", 200, "success", result, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi thống kê ngày", 500, "error", null, e.getMessage()));
        }
    }

    /**
     * Thống kê doanh thu theo THÁNG cụ thể
     */
    @GetMapping("/by-month")
    public ResponseEntity<ApiResponse<OrderRevenueDTO>> getRevenueByMonth(
            @RequestParam("year") int year,
            @RequestParam("month") int month,
            HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            OrderRevenueDTO result = orderService.getRevenueByMonth(year, month);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thống kê theo tháng thành công", 200, "success", result, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi thống kê tháng", 500, "error", null, e.getMessage()));
        }
    }

    /**
     * Thống kê doanh thu theo NĂM
     */
    @GetMapping("/by-year")
    public ResponseEntity<ApiResponse<OrderRevenueDTO>> getRevenueByYear(
            @RequestParam("year") int year,
            HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            OrderRevenueDTO result = orderService.getRevenueByYear(year);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thống kê theo năm thành công", 200, "success", result, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi thống kê năm", 500, "error", null, e.getMessage()));
        }
    }

    /**
     * Thống kê doanh thu theo TUẦN (theo khoảng ngày)
     */
    @GetMapping("/by-week")
    public ResponseEntity<ApiResponse<List<WeekOrderRevenueDTO>>> getRevenueByWeek(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
            HttpServletRequest request) {
        try {
            Boolean isAdmin = (Boolean) request.getAttribute("isAdmin");
            if (isAdmin == null || !isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse<>("Bạn không có quyền truy cập!", 403, "forbidden", null, null));
            }
            List<WeekOrderRevenueDTO> result = orderService.getRevenueByWeek(start, end);
            return ResponseEntity.ok(new ApiResponse<>("Lấy thống kê theo tuần thành công", 200, "success", result, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Lỗi thống kê tuần", 500, "error", null, e.getMessage()));
        }
    }

    @PostMapping("/place-order")
    public ResponseEntity<ApiResponse<String>> placeOrder(@RequestBody OrderResquest orderResquest,
                                                          HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ApiResponse<>("Người dùng chưa đăng nhập", 401, "unauthorized", null, null));
            }

            String message = orderService.placeOrder(userId, orderResquest);
            return ResponseEntity.ok(new ApiResponse<>(message, 200, "success", null, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("Đặt hàng thất bại", 500, "error", null, e.getMessage()));
        }
    }
}
