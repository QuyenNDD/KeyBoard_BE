package com.project.keyboard.repository.user;

import com.project.keyboard.dto.response.user.UserDTO;

import com.project.keyboard.entity.Users;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    protected Log log = LogFactory.getLog(getClass());

    @Override
    public List<UserDTO> getListUser(int page, int size){
        try {
            int offset = page * size;
            String sql = """
                    SELECT\s
                      u.user_id,
                      u.username,
                      u.email,
                      u.full_name,
                      u.phone,
                      u.address,
                      u.status,
                      u.created_at,
                      COUNT(o.order_id) AS total_orders,
                      IFNULL(SUM(o.total_amount), 0) AS total_spent
                    FROM users u
                    LEFT JOIN orders o ON u.user_id = o.user_id
                    WHERE u.user_id != 1
                    GROUP BY u.user_id
                    ORDER BY u.user_id
                    LIMIT ? OFFSET ?
                    """;
            return jdbcTemplate.query(sql, new Object[]{size, offset}, (rs, rowNum) -> {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getInt("user_id"));
                dto.setUsername(rs.getString("username"));
                dto.setEmail(rs.getString("email"));
                dto.setFullName(rs.getString("full_name"));
                dto.setPhone(rs.getString("phone"));
                dto.setAddress(rs.getString("address"));
                dto.setStatus(rs.getBoolean("status"));
                dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                dto.setTotalOrders(rs.getInt("total_orders"));
                dto.setTotalSpent(rs.getBigDecimal("total_spent"));
                return dto;
            });
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public UserDTO getUserDetail(int userId){
        try {
            String sql = """
                    SELECT\s
                      u.user_id,
                      u.username,
                      u.email,
                      u.full_name,
                      u.phone,
                      u.address,
                      u.status,
                      u.created_at,
                      COUNT(o.order_id) AS total_orders,
                      IFNULL(SUM(o.total_amount), 0) AS total_spent
                    FROM users u
                    LEFT JOIN orders o ON u.user_id = o.user_id
                    WHERE u.user_id = ?
                    """;

            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, (rs, rowNum) -> {
                UserDTO dto = new UserDTO();
                dto.setId(rs.getInt("user_id"));
                dto.setUsername(rs.getString("username"));
                dto.setEmail(rs.getString("email"));
                dto.setFullName(rs.getString("full_name"));
                dto.setPhone(rs.getString("phone"));
                dto.setAddress(rs.getString("address"));
                dto.setStatus(rs.getBoolean("status"));
                dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                dto.setTotalOrders(rs.getInt("total_orders"));
                dto.setTotalSpent(rs.getBigDecimal("total_spent"));
                return dto;
            });

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public int countUsers(){
        try {
            String sql = "SELECT COUNT(*) FROM users where user_id != 1";
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Users findById(int userId){
        try {
            String query = "SELECT * FROM users WHERE user_id = ?";
            return jdbcTemplate.queryForObject(query, BeanPropertyRowMapper.newInstance(Users.class), userId);
        }catch (EmptyResultDataAccessException e){
            return null;
        }
        catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void updateStatusUser(Users users){
        try{
            String query = "UPDATE users SET status = ? WHERE user_id = ?";
            jdbcTemplate.update(query, users.isStatus(), users.getUserId());
        }catch (Exception e){
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Users findByUsername(String username){
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Users user = new Users();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                user.setStatus(rs.getBoolean("status"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                user.setAdmin(rs.getBoolean("is_admin"));
                return user;
            }, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public Users findByEmail(String email){
        try {
            String sql = " SELECT * FROM users WHERE email = ?";
            return jdbcTemplate.queryForObject(sql, BeanPropertyRowMapper.newInstance(Users.class), email);
        }catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void save(Users users){
        try {
            String sql = """
            INSERT INTO users (username, password, email, full_name, status, phone, address, created_at, is_admin)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

            // Sử dụng KeyHolder để lấy ID tự sinh
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                var ps = connection.prepareStatement(sql, new String[]{"user_id"});
                ps.setString(1, users.getUsername());
                ps.setString(2, users.getPassword());
                ps.setString(3, users.getEmail());
                ps.setString(4, users.getFullName());
                ps.setBoolean(5, users.isStatus());
                ps.setString(6, users.getPhone());
                ps.setString(7, users.getAddress());
                ps.setTimestamp(8, java.sql.Timestamp.valueOf(users.getCreatedAt()));
                ps.setBoolean(9, users.isAdmin());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key != null) {
                users.setUserId(key.intValue());
            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean existByUsername(String username){
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
            return count != null && count > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean existByEmail(String email){
        try {
            String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
            return count != null && count > 0;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw e;
        }
    }

}
