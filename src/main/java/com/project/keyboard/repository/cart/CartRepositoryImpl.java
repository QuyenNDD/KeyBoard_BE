package com.project.keyboard.repository.cart;

import com.project.keyboard.dto.response.cart.CartUserResponse;
import com.project.keyboard.dto.response.cart.TotalCartDTO;
import com.project.keyboard.entity.Cart;
import com.project.keyboard.entity.ProductCategory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartRepositoryImpl implements CartRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public List<TotalCartDTO> getTotalCart() {
        try {
            String query = "SELECT COUNT(*) AS number, 'Total User' AS label FROM users\n" +
                    "UNION ALL\n" +
                    "SELECT COUNT(*) AS number, 'Total Order' AS label FROM orders\n" +
                    "UNION ALL\n" +
                    "SELECT IFNULL(SUM(total_amount), 0), 'Total Sales' FROM orders\n" +
                    "UNION ALL\n" +
                    "SELECT COUNT(*) AS number, 'Total Order Pending' FROM orders WHERE status = 'Pending' OR status = 'Shipping';\n";
            return jdbcTemplate.query(query, (rs, rowNum) -> {
                Number number = rs.getBigDecimal("number");
                String key = rs.getString("label");
                return new TotalCartDTO(number, key);
            });
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int addToCart(int userId, int variantId, int quantity) {
        try {
            String sql = "INSERT INTO cart (user_id, variant_id, quantity) VALUES (?, ?, ?)";
            return jdbcTemplate.update(sql, userId, variantId, quantity);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean isItemExist(int userId, int variantId) {
        try{
            String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ? AND variant_id = ?;";
            Integer count = jdbcTemplate.queryForObject(sql, new Object[]{userId, variantId}, Integer.class);
            return count > 0 && count != null;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int updateQuantity(int userId, int variantId, int quantity) {
        try {
            String sql = "UPDATE cart SET quantity = quantity + ? WHERE user_id = ? AND variant_id = ?";
            return jdbcTemplate.update(sql, quantity, userId, variantId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean isCartBelongToUser(int userId, int cartId){
        try {
            String sql = "SELECT COUNT(*) FROM cart WHERE cart_id = ? AND user_id = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cartId, userId);
            return count != null && count > 0;
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public List<CartUserResponse> getListCartBelongUser(int userId){
        try {
            String sql = """
                    SELECT  c.cart_id,
                            p.name,
                            pv.color,
                            p.brand,
                            pv.img,
                            pv.price,
                            c.quantity,
                            c.added_at
                    FROM cart c
                    JOIN product_variants pv ON pv.variant_id = c.variant_id
                    JOIN products p ON p.product_id = pv.product_id
                    WHERE c.user_id = ?
                    ORDER BY c.added_at DESC
                    """;

            return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
                CartUserResponse cart = new CartUserResponse();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setName(rs.getString("name"));
                cart.setColor(rs.getString("color"));
                cart.setBrand(rs.getString("brand"));
                cart.setImg(rs.getString("img"));
                cart.setPrice(rs.getBigDecimal("price"));
                cart.setQuantity(rs.getInt("quantity"));
                cart.setAddedAt(rs.getTimestamp("added_at").toLocalDateTime());
                return cart;
            });
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int countCartBelongUser(int userId){
        try{
            String sql = "SELECT COUNT(*) FROM cart WHERE user_id = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{userId},Integer.class);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Cart findById(int cartId, int userId){
        try {
            String sql = "SELECT * FROM cart WHERE cart_id = ? AND user_id = ?";
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Cart.class), cartId, userId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int deleteById(int cartId, int userId){
        try {
            String sql = "DELETE FROM cart WHERE cart_id = ? AND user_id = ?";
            return jdbcTemplate.update(sql, cartId, userId);
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }
}
