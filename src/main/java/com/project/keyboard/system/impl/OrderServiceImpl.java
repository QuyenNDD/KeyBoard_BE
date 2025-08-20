package com.project.keyboard.system.impl;

import com.project.keyboard.dto.request.CartItemRequest;
import com.project.keyboard.dto.request.GuestOrderRequest;
import com.project.keyboard.dto.request.OrderItemRequest;
import com.project.keyboard.dto.request.OrderResquest;
import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.revenue.DayOrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.dto.response.revenue.OrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.WeekOrderRevenueDTO;
import com.project.keyboard.dto.response.user.UserOrderResponse;
import com.project.keyboard.entity.Order;
import com.project.keyboard.entity.Product;
import com.project.keyboard.entity.ProductVariant;
import com.project.keyboard.entity.Users;
import com.project.keyboard.enums.OrderStatus;
import com.project.keyboard.repository.cart.CartRepository;
import com.project.keyboard.repository.order.OrderRepository;
import com.project.keyboard.repository.productVariant.ProductVariantRepository;
import com.project.keyboard.repository.user.UserRepository;
import com.project.keyboard.system.OrderService;
import jakarta.validation.constraints.Email;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private EmailService emailService;

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

    @Override
    public String placeOrder(int userId, OrderResquest resquest){
        Users user = userRepository.findById(userId);
        if (!user.isStatus()){
            return "Người dùng đã bị khóa tài khoản";
        }

        BigDecimal total = BigDecimal.ZERO;
        List<CartItemRequest> items = new ArrayList<>();
        for (OrderItemRequest item : resquest.getItems()) {
            if (!cartRepository.isCartBelongToUser(userId, item.getCartId())){
                return "Bạn không có quyền";
            }
            CartItemRequest cartItem = orderRepository.getCartItemById(item.getCartId());

            cartItem.setQuantity(item.getQuantity());
            BigDecimal price = orderRepository.getPriceByVariantId(cartItem.getVariantId());

            Integer stock = productVariantRepository.getStockByVariantId(cartItem.getVariantId());
            if (stock == null){
                stock = 0;
            }

            if (stock < cartItem.getQuantity()){
                ProductVariant productVariant = productVariantRepository.findById(cartItem.getVariantId());
                return "Sản phẩm " + productVariant.getProduct().getName() + " " + productVariant.getColor() + " không đủ số lượng. Còn lại " + stock + " sản phẩm";
            }

            BigDecimal itemsTotal = price.multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(itemsTotal);

            items.add(cartItem);
        }
        int orderId = orderRepository.insertOrder(userId, total, resquest.getPhone(), resquest.getAddress(), resquest.getEmail());

        for (CartItemRequest item : items){
            BigDecimal price = orderRepository.getPriceByVariantId(item.getVariantId());
            orderRepository.insertOrderDetail(orderId, item.getVariantId(), item.getQuantity(), price);
            orderRepository.deleteCartItem(item.getCartId());
        }

        List<String> itemLines = new ArrayList<>();
        for (CartItemRequest item : items) {
            ProductVariant variant = productVariantRepository.findById(item.getVariantId());
            Product product = variant.getProduct();

            String line = product.getName() + " (" + variant.getColor() + ") x " + item.getQuantity()
                    + " - " + String.format("%,.1f", orderRepository.getPriceByVariantId(item.getVariantId())) + " VNĐ";
            itemLines.add(line);
        }

        String orderCode = "DH" + orderId;

        emailService.sendOrderConfirmation(
                resquest.getEmail(),
                resquest.getPhone(),
                resquest.getAddress(),
                itemLines,
                total,
                orderCode
        );

        return "Đặt hàng thành công";
    }

    @Override
    public String placeGuestOrder(GuestOrderRequest request){
        Integer stock = productVariantRepository.getStockByVariantId(request.getVariantId());

        if (stock == null){
            stock = 0;
        }

        if (request.getQuantity() > stock || stock == null){
            return "Sản phẩm không đủ số lượng. Chỉ còn lại: " + (stock == null ? 0 : stock);
        }
        BigDecimal price = orderRepository.getPriceByVariantId(request.getVariantId());
        BigDecimal total = price.multiply(BigDecimal.valueOf(request.getQuantity()));

        int orderId = orderRepository.insertGuestOrder(total, request.getPhone(), request.getAddress(), request.getEmail());

        orderRepository.insertOrderDetail(orderId, request.getVariantId(), request.getQuantity(), price);

        ProductVariant variant = productVariantRepository.findById(request.getVariantId());
        Product product = variant.getProduct();

        List<String> itemLines = new ArrayList<>();
        itemLines.add(product.getName() + " (" + variant.getColor() + ") x " + request.getQuantity()
                + " - " + String.format("%,.1f", price) + " VNĐ");

        String orderCode = "GUEST" + orderId;

        emailService.sendOrderConfirmation(
                request.getEmail(),
                request.getPhone(),
                request.getAddress(),
                itemLines,
                total,
                orderCode
        );

        return "Đặt hàng thành công";
    }
}
