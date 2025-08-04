package com.project.keyboard.repository.cart;

import com.project.keyboard.dto.response.cart.TotalCartDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
}
