package com.project.keyboard.repository.order;

import com.project.keyboard.dto.response.revenue.MonthlyOrderCount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
}
