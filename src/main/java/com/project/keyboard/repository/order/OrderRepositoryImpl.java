package com.project.keyboard.repository.order;

import com.project.keyboard.dto.request.CartItemRequest;
import com.project.keyboard.dto.response.order.OrderDetailResponse;
import com.project.keyboard.dto.response.order.OrderResponse;
import com.project.keyboard.dto.response.revenue.DayOrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import com.project.keyboard.dto.response.revenue.OrderRevenueDTO;
import com.project.keyboard.dto.response.revenue.WeekOrderRevenueDTO;
import com.project.keyboard.dto.response.user.UserOrderResponse;
import com.project.keyboard.entity.Order;
import com.project.keyboard.enums.OrderStatus;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public List<MonthlyOrderCount> getMonthlyOrderCount(int year){
        try {
            String query = """
            SELECT MONTH(order_date) AS month, COUNT(order_id) AS totalOrders
            FROM orders
            WHERE YEAR(order_date) = ?
            GROUP BY MONTH(order_date)
            ORDER BY month
        """;

            return jdbcTemplate.query(
                    query,
                    new Object[]{year},
                    (rs, rowNum) -> {
                        MonthlyOrderCount dto = new MonthlyOrderCount();
                        dto.setMonth(rs.getInt("month"));
                        dto.setTotalOrders(rs.getInt("totalOrders"));
                        return dto;
                    }
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OrderResponse> getListOrder(int page, int size){
        try{
            int offset = page * size;
            String sql = """
            SELECT o.order_id, o.full_name, o.order_date, o.total_amount, o.status, o.phone, o.address, o.email, o.shipping_fee
            FROM orders o
            ORDER BY o.order_date DESC
            LIMIT ? OFFSET ?
            """;

            return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> {
                OrderResponse order = new OrderResponse();
                order.setId(rs.getInt("order_id"));
                order.setFullName(rs.getString("full_name"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setPrice(rs.getBigDecimal("total_amount"));
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                order.setPhone(rs.getString("phone"));
                order.setAddress(rs.getString("address"));
                order.setEmail(rs.getString("email"));
                order.setShippingFee(rs.getBigDecimal("shipping_fee"));
                return order;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }


    @Override
    public int countOrders() {
        String sql = "SELECT COUNT(*) FROM orders";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    @Override
    public List<OrderResponse> getOrdersByUserId(int userId, int page, int size){
        try {
            int offset = page * size;
            String sql = """
            SELECT o.order_id, o.full_name, o.order_date, o.total_amount, o.status, o.phone, o.address, o.email
            FROM orders o
            JOIN users u ON o.user_id = u.user_id
            WHERE o.user_id = ?
            ORDER BY o.order_date DESC
            LIMIT ? OFFSET ?
        """;

            return jdbcTemplate.query(sql, new Object[]{userId, size, offset}, (rs, rowNum) -> {
                OrderResponse order = new OrderResponse();
                order.setId(rs.getInt("order_id"));
                order.setFullName(rs.getString("full_name"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setPrice(rs.getBigDecimal("total_amount"));
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                order.setPhone(rs.getString("phone"));
                order.setAddress(rs.getString("address"));
                order.setEmail(rs.getString("email"));
                return order;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OrderResponse> getOrdersByExactDate(String date, int page, int size) {
        try {
            int offset = page * size;

            String sql = """
        SELECT o.order_id, o.full_name, o.order_date, o.total_amount, o.status, o.phone, o.address, o.email
        FROM orders o
        WHERE DATE(o.order_date) = ?
        ORDER BY o.order_date DESC
        LIMIT ? OFFSET ?
    """;

            return jdbcTemplate.query(sql, new Object[]{date, size, offset}, (rs, rowNum) -> {
                OrderResponse order = new OrderResponse();
                order.setId(rs.getInt("order_id"));
                order.setFullName(rs.getString("full_name"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setPrice(rs.getBigDecimal("total_amount"));
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                order.setPhone(rs.getString("phone"));
                order.setAddress(rs.getString("address"));
                order.setEmail(rs.getString("email"));
                return order;
            });
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public int countOrdersByExactDate(String date) {
        try {
            String sql = """
        SELECT COUNT(*)
        FROM orders
        WHERE DATE(order_date) = ?
    """;
            return jdbcTemplate.queryForObject(sql, new Object[]{date}, Integer.class);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public OrderResponse getOrderById(int id){
        try{
            String sql = """
        SELECT  o.order_id, o.full_name, o.order_date, o.total_amount, o.status, o.phone, o.address, o.email, o.shipping_fee
                                          	FROM orders o
                                            WHERE o.order_id = ?
    """;

            OrderResponse dto = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id},
                    (rs, rowNum) -> {
                        OrderResponse o = new OrderResponse();
                        o.setId(rs.getInt("order_id"));
                        o.setFullName(rs.getString("full_name"));
                        o.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                        o.setPrice(rs.getBigDecimal("total_amount"));
                        o.setPhone(rs.getString("phone"));
                        o.setAddress(rs.getString("address"));
                        o.setEmail(rs.getString("email"));
                        o.setStatus(OrderStatus.valueOf(rs.getString("status")));
                        o.setShippingFee(rs.getBigDecimal("shipping_fee"));
                        return o;
                    }
            );

            // Details (theo variant_id, product_name, color)
            String detailSql = """
        SELECT 
          od.variant_id,
          p.name AS product_name,
          pv.color,
          pv.img,
          od.quantity,
          od.price
        FROM 
          order_details od
        JOIN 
          product_variants pv ON pv.variant_id = od.variant_id
        JOIN 
          products p ON pv.product_id = p.product_id
        WHERE 
          od.order_id = ?
    """;

            List<OrderDetailResponse> details = jdbcTemplate.query(
                    detailSql,
                    new Object[]{id},
                    (rs, rowNum) -> {
                        OrderDetailResponse d = new OrderDetailResponse();
                        d.setVariantId(rs.getInt("variant_id"));
                        d.setProductName(rs.getString("product_name"));
                        d.setColor(rs.getString("color"));
                        d.setImg(rs.getString("img"));
                        d.setQuantity(rs.getInt("quantity"));
                        d.setPrice(rs.getBigDecimal("price"));
                        return d;
                    }
            );

            assert dto != null;
            dto.setOrderDetails(details);
            return dto;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public OrderResponse getOrderByIdForUser(int id, int userId) {
        try {
            String sql = """
            SELECT o.order_id, o.full_name, o.order_date, o.total_amount, o.status, o.phone, o.address, o.email
            FROM orders o
            WHERE o.order_id = ? AND o.user_id = ?
        """;

            OrderResponse dto = jdbcTemplate.queryForObject(
                    sql,
                    new Object[]{id, userId},
                    (rs, rowNum) -> {
                        OrderResponse o = new OrderResponse();
                        o.setId(rs.getInt("order_id"));
                        o.setFullName(rs.getString("full_name"));
                        o.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                        o.setPrice(rs.getBigDecimal("total_amount"));
                        o.setPhone(rs.getString("phone"));
                        o.setAddress(rs.getString("address"));
                        o.setEmail(rs.getString("email"));
                        o.setStatus(OrderStatus.valueOf(rs.getString("status")));
                        return o;
                    }
            );

            if (dto == null) return null;

            // Lấy danh sách chi tiết sản phẩm trong đơn
            String detailSql = """
            SELECT 
              od.variant_id,
              p.name AS product_name,
              pv.color,
              pv.img,
              od.quantity,
              od.price
            FROM 
              order_details od
            JOIN 
              product_variants pv ON pv.variant_id = od.variant_id
            JOIN 
              products p ON pv.product_id = p.product_id
            WHERE 
              od.order_id = ?
        """;

            List<OrderDetailResponse> details = jdbcTemplate.query(
                    detailSql,
                    new Object[]{id},
                    (rs, rowNum) -> {
                        OrderDetailResponse d = new OrderDetailResponse();
                        d.setVariantId(rs.getInt("variant_id"));
                        d.setProductName(rs.getString("product_name"));
                        d.setColor(rs.getString("color"));
                        d.setImg(rs.getString("img"));
                        d.setQuantity(rs.getInt("quantity"));
                        d.setPrice(rs.getBigDecimal("price"));
                        return d;
                    }
            );

            dto.setOrderDetails(details);
            return dto;
        } catch (EmptyResultDataAccessException e) {
            return null; // Không có đơn hàng hoặc không thuộc user
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }


    @Override
    public void updateOrderStatus(int orderId, String status){
        try{
            String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
            jdbcTemplate.update(sql, status, orderId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public DayOrderRevenueDTO getRevenueByDay(LocalDate date) {
        try {
            String sql = """
            SELECT DATE(order_date) AS date,
                   SUM(total_amount) AS totalRevenue,
                   COUNT(order_id) AS totalOrders
            FROM orders
            WHERE status = 'COMPLETED'
              AND DATE(order_date) = ?
            GROUP BY DATE(order_date)
        """;

            List<DayOrderRevenueDTO> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
                DayOrderRevenueDTO dto = new DayOrderRevenueDTO();
                dto.setDate(rs.getString("date"));
                dto.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
                dto.setTotalOrders(rs.getLong("totalOrders"));
                return dto;
            }, date);

            return list.stream().findFirst().orElse(null);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public OrderRevenueDTO  getRevenueByMonth(int year, Integer month) {
        try {
            String sql = """
            SELECT YEAR(order_date) AS year,
                   MONTH(order_date) AS month,
                   SUM(total_amount) AS totalRevenue,
                   COUNT(order_id) AS totalOrders
            FROM orders
            WHERE status = 'COMPLETED'
              AND YEAR(order_date) = ?
              AND MONTH(order_date) = ?
            GROUP BY YEAR(order_date), MONTH(order_date)
        """;

            return jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> {
                        OrderRevenueDTO dto = new OrderRevenueDTO();
                        dto.setYear(rs.getInt("year"));
                        dto.setMonth(rs.getInt("month"));
                        dto.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
                        dto.setTotalOrders(rs.getLong("totalOrders"));
                        return dto;
                    },
                    year, month
            );

        } catch (EmptyResultDataAccessException e) {
            return null; // Hoặc trả DTO rỗng
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }

    }

    @Override
    public OrderRevenueDTO getRevenueByYear(int year) {
        try {
            String sql = """
            SELECT YEAR(order_date) AS year,
                   SUM(total_amount) AS totalRevenue,
                   COUNT(order_id) AS totalOrders
            FROM orders
            WHERE status = 'COMPLETED'
              AND YEAR(order_date) = ?
            GROUP BY YEAR(order_date)
        """;

            return jdbcTemplate.query(sql, rs -> {
                if (rs.next()) {
                    OrderRevenueDTO dto = new OrderRevenueDTO();
                    dto.setYear(rs.getInt("year"));
                    dto.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
                    dto.setTotalOrders(rs.getLong("totalOrders"));
                    return dto;
                } else {
                    return null; // KHÔNG có dữ liệu => trả null
                }
            }, year);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<WeekOrderRevenueDTO> getRevenueByWeek(LocalDate start, LocalDate end){
        try {
            String sql = """
            SELECT 
                WEEK(order_date, 1) AS week,
                DATE_SUB(order_date, INTERVAL WEEKDAY(order_date) DAY) AS weekStartDate,
                SUM(total_amount) AS totalRevenue,
                COUNT(order_id) AS totalOrders
            FROM orders
            WHERE status = 'COMPLETED'
              AND DATE(order_date) BETWEEN ? AND ?
            GROUP BY WEEK(order_date, 1), YEAR(order_date), weekStartDate
            ORDER BY week
        """;

            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                WeekOrderRevenueDTO dto = new WeekOrderRevenueDTO();
                dto.setWeek(rs.getInt("week"));
                dto.setWeekStartDate(rs.getDate("weekStartDate").toLocalDate());
                dto.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
                dto.setTotalOrders(rs.getLong("totalOrders"));
                return dto;
            }, start, end);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }

    }
    @Override
    public List<UserOrderResponse> getOrdersByUser(int userId, int page, int size){
        try {
            int offset = page * size;

            String sql = """
            SELECT order_id, address, total_amount, order_date, status
            FROM orders
            WHERE user_id = ?
            ORDER BY order_date DESC
            LIMIT ? OFFSET ?
        """;

            return jdbcTemplate.query(sql, new Object[]{userId, size, offset}, (rs, rowNum) -> {
                UserOrderResponse dto = new UserOrderResponse();
                dto.setId(rs.getInt("order_id"));
                dto.setAddress(rs.getString("address"));
                dto.setTotalAmount(rs.getBigDecimal("total_amount"));
                dto.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                dto.setStatus(rs.getString("status"));
                return dto;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
    @Override
    public int countOrdersByUser(int userId){
        try {
            String countSql = "SELECT COUNT(*) FROM orders WHERE user_id = ?";
            return jdbcTemplate.queryForObject(countSql, new Object[]{userId}, Integer.class);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
    @Override
    public CartItemRequest getCartItemById(int cartId) {
        try {
            String sql = "SELECT * FROM cart WHERE cart_id = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CartItemRequest.class), cartId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public BigDecimal getPriceByVariantId(int variantId) {
        try{
            String sql = "SELECT price FROM product_variants WHERE variant_id = ?";
            return jdbcTemplate.queryForObject(sql, BigDecimal.class, variantId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw  e;
        }
    }

    @Override
    public int insertOrder(int userId, BigDecimal total, String phone, String address, String email, String fullName) {
        try {
            String sql = """
                         INSERT INTO orders(user_id, order_date, total_amount, phone, address, email, status, full_name)
                         VALUES (?, NOW(), ?, ?, ?, ?, 'PENDING', ?)
                        """;
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, userId);
                ps.setBigDecimal(2, total);
                ps.setString(3, phone);
                ps.setString(4, address);
                ps.setString(5, email);
                ps.setString(6, fullName);
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void insertOrderDetail(int orderId, int variantId, int quantity, BigDecimal price) {
        try {
            String sql = "INSERT INTO order_details(order_id, variant_id, quantity, price) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, orderId, variantId, quantity, price);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCartItem(int cartId){
        try {
            String sql = "DELETE FROM cart WHERE cart_id = ?";
            jdbcTemplate.update(sql, cartId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int insertGuestOrder(BigDecimal total, String phone, String address, String email, String fullName){
        try {
            String sql = "INSERT INTO orders(user_id, order_date, total_amount, phone, address, email, status, full_name)\n" +
                    "                         VALUES (NULL, NOW(), ?, ?, ?, ?, 'PENDING', ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setBigDecimal(1, total);
                ps.setString(2, phone);
                ps.setString(3, address);
                ps.setString(4, email);
                ps.setString(5, fullName);
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue();
        }catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
