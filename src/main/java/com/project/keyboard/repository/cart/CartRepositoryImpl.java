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
                    "SELECT COUNT(*) AS number, 'Total Order Pending' FROM orders WHERE status = 'Pending';\n";
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
}
